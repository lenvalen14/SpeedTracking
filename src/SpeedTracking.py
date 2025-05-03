from fastapi import FastAPI, Form
from fastapi.responses import JSONResponse
import os, uuid, cv2, numpy as np
import requests
from ultralytics import YOLO
import supervision as sv
from collections import defaultdict, deque
from dotenv import load_dotenv
import cloudinary
import cloudinary.uploader
from paddleocr import PaddleOCR

# Load .env + cấu hình Cloudinary
load_dotenv(dotenv_path="src/.env")
cloudinary.config(
    cloud_name=os.getenv("CLOUDINARY_CLOUD_NAME"),
    api_key=os.getenv("CLOUDINARY_API_KEY"),
    api_secret=os.getenv("CLOUDINARY_API_SECRET"),
    secure=True
)

app = FastAPI()

# Khai báo vùng zone để tính tốc độ
SOURCE = np.array([[1252, 787], [2298, 803], [5039, 2159], [-550, 2159]])
TARGET_WIDTH, TARGET_HEIGHT = 25, 250
TARGET = np.array([[0, 0], [TARGET_WIDTH - 1, 0], [TARGET_WIDTH - 1, TARGET_HEIGHT - 1], [0, TARGET_HEIGHT - 1]])

ocr_model = PaddleOCR(use_angle_cls=True, lang='en', show_log=False)

# Hàm nhận diện biển số
def detect_license_plate(image: np.ndarray) -> str:
    result = ocr_model.ocr(image, cls=True)
    if result and result[0]:
        texts = [line[1][0] for line in result[0] if line[1][1] > 0.5]  # confidence > 50%
        return " ".join(texts)
    return "Unknown"

# Hàm tải video từ URL
def download_video_from_url(url: str, local_path: str) -> bool:
    try:
        response = requests.get(url, stream=True)
        response.raise_for_status()
        
        with open(local_path, 'wb') as f:
            for chunk in response.iter_content(chunk_size=8192):
                f.write(chunk)
        return True
    except Exception as e:
        print(f"Error downloading video: {e}")
        return False

# Xử lý transform
class ViewTransformer:
    def __init__(self, source: np.ndarray, target: np.ndarray) -> None:
        self.m = cv2.getPerspectiveTransform(source.astype(np.float32), target.astype(np.float32))

    def transform_points(self, points: np.ndarray) -> np.ndarray:
        if points.size == 0:
            return points
        reshaped_points = points.reshape(-1, 1, 2).astype(np.float32)
        transformed_points = cv2.perspectiveTransform(reshaped_points, self.m)
        return transformed_points.reshape(-1, 2)

# Endpoint chính - nhận video URL thay vì file upload
@app.post("/detect-speed-from-url")
async def detect_speed_from_url(video_url: str = Form(...), speed_limit: float = Form(...)):
    file_id = str(uuid.uuid4())
    input_path = f"temp_{file_id}.mp4"
    output_path = f"output_{file_id}.mp4"
    violation_dir = f"violations_{file_id}"
    os.makedirs(violation_dir, exist_ok=True)

    try:
        # Tải video từ URL
        download_success = download_video_from_url(video_url, input_path)
        if not download_success:
            return JSONResponse(status_code=500, content={"error": "Failed to download video from URL"})

        model = YOLO("model/yolov8x.pt")
        video_info = sv.VideoInfo.from_video_path(input_path)
        byte_track = sv.ByteTrack(frame_rate=video_info.fps, track_activation_threshold=0.3)
        annotators = {
            "box": sv.BoxAnnotator(thickness=2),
            "label": sv.LabelAnnotator(text_position=sv.Position.BOTTOM_CENTER),
            "trace": sv.TraceAnnotator(trace_length=video_info.fps * 2, position=sv.Position.BOTTOM_CENTER)
        }

        transformer = ViewTransformer(source=SOURCE, target=TARGET)
        polygon_zone = sv.PolygonZone(polygon=SOURCE)
        frame_gen = sv.get_video_frames_generator(source_path=input_path)

        coordinates = defaultdict(lambda: deque(maxlen=video_info.fps))
        time_records = defaultdict(lambda: deque(maxlen=video_info.fps))
        frame_count = 0
        processed_vehicles, processed_violators = {}, {}
        best_violation_images = {}
        all_vehicle_plates = {}
        vehicle_class = {1: "bicycle", 2: "car", 3: "motorcycle", 5: "bus", 7: "truck"}

        with sv.VideoSink(output_path, video_info) as sink:
            for frame in frame_gen:
                frame_count += 1
                result = model(frame)[0]
                detections = sv.Detections.from_ultralytics(result)
                detections = detections[detections.confidence > 0.3]
                detections = detections[polygon_zone.trigger(detections)].with_nms(threshold=0.7)
                detections = byte_track.update_with_detections(detections=detections)
                points = transformer.transform_points(detections.get_anchors_coordinates(anchor=sv.Position.BOTTOM_CENTER)).astype(int)

                for tracker_id, point in zip(detections.tracker_id, points):
                    coordinates[tracker_id].append(point)
                    time_records[tracker_id].append(frame_count / video_info.fps)

                labels, colors = [], []

                for i in range(len(detections.tracker_id)):
                    tracker_id = detections.tracker_id[i]
                    box = detections.xyxy[i]
                    class_id = detections.class_id[i] if hasattr(detections, 'class_id') else None

                    x1, y1, x2, y2 = map(int, box)
                    cropped = frame[y1:y2, x1:x2]

                    # Nhận diện biển số nếu chưa có
                    if tracker_id not in all_vehicle_plates:
                        plate = detect_license_plate(cropped)
                        if plate != "Unknown":
                            all_vehicle_plates[tracker_id] = {
                                "license_plate": plate,
                                "vehicle_type": vehicle_class.get(int(class_id), "unknown") if class_id else "unknown"
                            }

                    if len(coordinates[tracker_id]) < video_info.fps / 2:
                        labels.append(f"#{tracker_id}")
                        colors.append((0, 255, 0))
                    else:
                        start, end = coordinates[tracker_id][0], coordinates[tracker_id][-1]
                        distance = np.linalg.norm(np.array(end) - np.array(start))
                        time_elapsed = time_records[tracker_id][-1] - time_records[tracker_id][0]

                        if time_elapsed > 0:
                            speed = min((distance / time_elapsed) * 3.6, 150.0)
                            labels.append(f"#{tracker_id} {int(speed)} km/h")

                            if tracker_id not in processed_vehicles or speed > processed_vehicles[tracker_id]["speed"]:
                                vehicle_type = vehicle_class.get(int(class_id), "unknown") if class_id else "unknown"
                                processed_vehicles[tracker_id] = {
                                    "tracker_id": int(tracker_id),
                                    "speed": round(speed, 2),
                                    "type": vehicle_type
                                }

                            if speed > speed_limit:
                                area = (x2 - x1) * (y2 - y1)
                                if (tracker_id not in best_violation_images) or (area > best_violation_images[tracker_id]["area"]):
                                    best_violation_images[tracker_id] = {
                                        "image": cropped,
                                        "speed": speed,
                                        "area": area
                                    }
                                colors.append((0, 0, 255))
                            else:
                                colors.append((0, 255, 0))
                        else:
                            labels.append(f"#{tracker_id}")
                            colors.append((0, 255, 0))

                frame = annotators["trace"].annotate(frame.copy(), detections)
                frame = annotators["box"].annotate(frame, detections=detections)
                frame = annotators["label"].annotate(frame, detections, labels=labels)
                cv2.polylines(frame, [SOURCE.reshape((-1, 1, 2))], True, (0, 255, 255), 2)
                sink.write_frame(frame)

        # OCR + Upload Cloudinary cho xe vi phạm
        for tracker_id, data in best_violation_images.items():
            save_path = os.path.join(violation_dir, f"{int(tracker_id)}_{int(data['speed'])}.jpg")
            cv2.imwrite(save_path, data["image"])
            uploaded = cloudinary.uploader.upload(save_path, folder="violations")
            plate_text = detect_license_plate(data["image"])

            processed_violators[tracker_id] = {
                "tracker_id": int(tracker_id),
                "speed": round(data["speed"], 2),
                "image_url": uploaded["secure_url"],
                "license_plate": plate_text
            }

        # Gán biển số cho tất cả phương tiện vào danh sách
        for tracker_id, info in processed_vehicles.items():
            plate_info = all_vehicle_plates.get(tracker_id)
            if plate_info:
                info["license_plate"] = plate_info["license_plate"]

        uploaded_video = cloudinary.uploader.upload(output_path, resource_type="video", folder="videos")

        # Tính tốc độ trung bình
        if processed_vehicles:
            speed_avg = round(
                sum(v["speed"] for v in processed_vehicles.values()) / len(processed_vehicles), 2
            )
        else:
            speed_avg = 0.0

        return JSONResponse(content={
            "violatorsCount": len(processed_violators),
            "violators": list(processed_violators.values()),
            "vehicles": list(processed_vehicles.values()),
            "vehiclesCount": len(processed_vehicles),
            "speedAvg": speed_avg,
            "output_video_url": uploaded_video["secure_url"]
        })

    except Exception as e:
        return JSONResponse(status_code=500, content={"error": str(e)})

    finally:
        for path in [input_path, output_path]:
            if os.path.exists(path):
                os.remove(path)
        
        import shutil
        if os.path.exists(violation_dir):
            shutil.rmtree(violation_dir)