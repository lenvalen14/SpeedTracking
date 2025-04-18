from fastapi import FastAPI, File, UploadFile, Form
from fastapi.responses import JSONResponse
import os
import uuid
import cv2
import numpy as np
from ultralytics import YOLO
import supervision as sv
from collections import defaultdict, deque
from dotenv import load_dotenv
import cloudinary
import cloudinary.uploader

load_dotenv(dotenv_path="src/.env")

cloudinary.config(
    cloud_name=os.getenv("CLOUDINARY_CLOUD_NAME"),
    api_key=os.getenv("CLOUDINARY_API_KEY"),
    api_secret=os.getenv("CLOUDINARY_API_SECRET"),
    secure=True
)

app = FastAPI()

SOURCE = np.array([[1252, 787], [2298, 803], [5039, 2159], [-550, 2159]])
TARGET_WIDTH, TARGET_HEIGHT = 25, 250
TARGET = np.array([[0, 0], [TARGET_WIDTH - 1, 0], [TARGET_WIDTH - 1, TARGET_HEIGHT - 1], [0, TARGET_HEIGHT - 1]])

class ViewTransformer:
    def __init__(self, source: np.ndarray, target: np.ndarray) -> None:
        self.m = cv2.getPerspectiveTransform(source.astype(np.float32), target.astype(np.float32))

    def transform_points(self, points: np.ndarray) -> np.ndarray:
        if points.size == 0:
            return points
        reshaped_points = points.reshape(-1, 1, 2).astype(np.float32)
        transformed_points = cv2.perspectiveTransform(reshaped_points, self.m)
        return transformed_points.reshape(-1, 2)

@app.post("/detect-speed")
async def detect_speed(video_file: UploadFile = File(...), speed_limit: float = Form(...)):
    file_id = str(uuid.uuid4())
    input_path = f"temp_{file_id}.mp4"
    output_path = f"output_{file_id}.mp4"
    violation_dir = f"violations_{file_id}"
    os.makedirs(violation_dir, exist_ok=True)

    try:
        with open(input_path, "wb") as f:
            f.write(await video_file.read())

        model = YOLO("model/yolov8x.pt")
        video_info = sv.VideoInfo.from_video_path(input_path)
        byte_track = sv.ByteTrack(frame_rate=video_info.fps, track_activation_threshold=0.3)
        box_annotator = sv.BoxAnnotator(thickness=2)
        label_annotator = sv.LabelAnnotator(text_position=sv.Position.BOTTOM_CENTER)
        trace_annotator = sv.TraceAnnotator(trace_length=video_info.fps * 2, position=sv.Position.BOTTOM_CENTER)
        view_transformer = ViewTransformer(source=SOURCE, target=TARGET)
        polygon_zone = sv.PolygonZone(polygon=SOURCE)
        frame_generator = sv.get_video_frames_generator(source_path=input_path)

        coordinates = defaultdict(lambda: deque(maxlen=video_info.fps))
        time_records = defaultdict(lambda: deque(maxlen=video_info.fps))
        frame_count = 0
        processed_vehicles = {}
        processed_violators = {}

        vehicle_class = {1: "bicycle", 2: "car", 3: "motorcycle", 5: "bus", 7: "truck"}

        with sv.VideoSink(output_path, video_info) as sink:
            for frame in frame_generator:
                frame_count += 1
                result = model(frame)[0]
                detections = sv.Detections.from_ultralytics(result)
                detections = detections[detections.confidence > 0.3]
                detections = detections[polygon_zone.trigger(detections)].with_nms(threshold=0.7)
                detections = byte_track.update_with_detections(detections=detections)
                points = view_transformer.transform_points(detections.get_anchors_coordinates(anchor=sv.Position.BOTTOM_CENTER)).astype(int)

                for tracker_id, point in zip(detections.tracker_id, points):
                    coordinates[tracker_id].append(point)
                    time_records[tracker_id].append(frame_count / video_info.fps)

                labels, colors = [], []

                for i in range(len(detections.tracker_id)):
                    tracker_id = detections.tracker_id[i]
                    box = detections.xyxy[i]
                    class_id = detections.class_id[i] if hasattr(detections, 'class_id') else None

                    if len(coordinates[tracker_id]) < video_info.fps / 2:
                        labels.append(f"#{tracker_id}")
                        colors.append((0, 255, 0))
                    else:
                        start_point, end_point = coordinates[tracker_id][0], coordinates[tracker_id][-1]
                        distance = np.linalg.norm(np.array(end_point) - np.array(start_point))
                        time_elapsed = time_records[tracker_id][-1] - time_records[tracker_id][0]

                        if time_elapsed > 0:
                            speed = min((distance / time_elapsed) * 3.6, 150.0)
                            labels.append(f"#{tracker_id} {int(speed)} km/h")

                            if tracker_id not in processed_vehicles or speed > processed_vehicles[tracker_id]["speed"]:
                                vehicle_type = vehicle_class.get(int(class_id), "unknown") if class_id else "unknown"
                                processed_vehicles[tracker_id] = {"tracker_id": int(tracker_id), "speed": round(speed, 2), "type": vehicle_type}

                            if speed > speed_limit:
                                if tracker_id not in processed_violators or speed > processed_violators[tracker_id]["speed"]:
                                    x1, y1, x2, y2 = map(int, box)
                                    cropped = frame[y1:y2, x1:x2]
                                    save_path = os.path.join(violation_dir, f"{int(tracker_id)}_{int(speed)}.jpg")
                                    cv2.imwrite(save_path, cropped)

                                    # Upload ảnh vi phạm
                                    uploaded_image = cloudinary.uploader.upload(save_path, folder="violations")
                                    processed_violators[tracker_id] = {
                                        "tracker_id": int(tracker_id),
                                        "speed": round(speed, 2),
                                        "image_url": uploaded_image["secure_url"]
                                    }
                                colors.append((0, 0, 255))
                            else:
                                colors.append((0, 255, 0))
                        else:
                            labels.append(f"#{tracker_id}")
                            colors.append((0, 255, 0))

                annotated_frame = trace_annotator.annotate(frame.copy(), detections)
                annotated_frame = box_annotator.annotate(annotated_frame, detections=detections)
                annotated_frame = label_annotator.annotate(annotated_frame, detections, labels=labels)
                cv2.polylines(annotated_frame, [SOURCE.reshape((-1, 1, 2))], True, (0, 255, 255), 2)
                sink.write_frame(annotated_frame)

        # Upload video đầu ra
        uploaded_video = cloudinary.uploader.upload(output_path, resource_type="video", folder="videos")

        return JSONResponse(content={
            "violators": list(processed_violators.values()),
            "vehicles": list(processed_vehicles.values()),
            "output_video_url": uploaded_video["secure_url"]
        })

    except Exception as e:
        return JSONResponse(status_code=500, content={"error": str(e)})
    finally:
        for path in [input_path, output_path]:
            if os.path.exists(path):
                os.remove(path)
