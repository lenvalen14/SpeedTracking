from fastapi import FastAPI, File, UploadFile, Form
from fastapi.responses import JSONResponse
import os
import uuid
import cv2
import numpy as np
from ultralytics import YOLO
import supervision as sv
from collections import defaultdict, deque

app = FastAPI()

SOURCE = np.array([[1252, 787], [2298, 803], [5039, 2159], [-550, 2159]])
TARGET_WIDTH, TARGET_HEIGHT = 25, 250
TARGET = np.array([[0, 0], [TARGET_WIDTH - 1, 0], [TARGET_WIDTH - 1, TARGET_HEIGHT - 1], [0, TARGET_HEIGHT - 1]])

class ViewTransformer:
    def __init__(self, source: np.ndarray, target: np.ndarray) -> None:
        source = source.astype(np.float32)
        target = target.astype(np.float32)
        self.m = cv2.getPerspectiveTransform(source, target)

    def transform_points(self, points: np.ndarray) -> np.ndarray:
        if points.size == 0:
            return points
        reshaped_points = points.reshape(-1, 1, 2).astype(np.float32)
        transformed_points = cv2.perspectiveTransform(reshaped_points, self.m)
        return transformed_points.reshape(-1, 2)

@app.post("/detect-speed")
async def detect_speed(video_file: UploadFile = File(...), speed_limit: float = Form(...)):
    try:
        file_id = str(uuid.uuid4())
        input_path = f"temp_{file_id}.mp4"
        output_path = f"output_{file_id}.mp4"
        violation_dir = f"violations_{file_id}"
        os.makedirs(violation_dir, exist_ok=True)

        with open(input_path, "wb") as f:
            f.write(await video_file.read())

        os.makedirs("model", exist_ok=True)
        model_path = "model/yolov8x.pt"

        model = YOLO(model_path)
        video_info = sv.VideoInfo.from_video_path(input_path)
        byte_track = sv.ByteTrack(frame_rate=video_info.fps, track_activation_threshold=0.3)
        box_annotator = sv.BoxAnnotator(thickness=2)
        label_annotator = sv.LabelAnnotator(text_position=sv.Position.BOTTOM_CENTER)
        trace_annotator = sv.TraceAnnotator(trace_length=video_info.fps * 2, position=sv.Position.BOTTOM_CENTER)
        view_transformer = ViewTransformer(source=SOURCE, target=TARGET)
        polygon_zone = sv.PolygonZone(polygon=SOURCE)
        frame_generator = sv.get_video_frames_generator(source_path=input_path)
        coordinates = defaultdict(lambda: deque(maxlen=video_info.fps))
        processed_vehicles_set = set()
        processed_vehicles = []

        processed_violators_set = set()
        processed_violators = []

        vehicle_class = {
            1: "bicycle",
            2: "car",
            3: "motorcycle",
            5: "bus",
            7: "truck"
        }

        with sv.VideoSink(output_path, video_info) as sink:
            for frame in frame_generator:
                result = model(frame)[0]
                detections = sv.Detections.from_ultralytics(result)
                detections = detections[detections.confidence > 0.3]
                detections = detections[polygon_zone.trigger(detections)]
                detections = detections.with_nms(threshold=0.7)
                detections = byte_track.update_with_detections(detections=detections)
                points = detections.get_anchors_coordinates(anchor=sv.Position.BOTTOM_CENTER)
                points = view_transformer.transform_points(points=points).astype(int)

                for tracker_id, [_, y] in zip(detections.tracker_id, points):
                    coordinates[tracker_id].append(y)

                labels, colors = [], []
                
                for i in range(len(detections.tracker_id)):
                    tracker_id = detections.tracker_id[i]
                    box = detections.xyxy[i]
                    class_id = detections.class_id[i] if hasattr(detections, 'class_id') else None
                    
                    if len(coordinates[tracker_id]) < video_info.fps / 2:
                        labels.append(f"#{tracker_id}")
                        colors.append((0, 255, 0))
                    else:
                        y_end = coordinates[tracker_id][-1]
                        y_start = coordinates[tracker_id][0]
                        distance = abs(y_start - y_end)
                        time = len(coordinates[tracker_id]) / video_info.fps
                        speed = distance / time * 3.6
                        labels.append(f"#{tracker_id} {int(speed)} km/h")

                        vehicle_key = (int(tracker_id), round(speed, 2))

                        if vehicle_key not in processed_vehicles_set:
                            processed_vehicles_set.add(vehicle_key)
                            vehicle_type = "unknown"
                            # Fix: Only try to get vehicle type if class_id is available
                            if class_id is not None:
                                vehicle_type = vehicle_class.get(int(class_id), "unknown")
                            
                            processed_vehicles.append({
                                "tracker_id": int(tracker_id),
                                "speed": round(speed, 2),
                                "type": vehicle_type,
                            })

                        if speed > speed_limit and vehicle_key not in processed_violators_set:
                            processed_violators_set.add(vehicle_key)
                            x1, y1, x2, y2 = map(int, box)
                            cropped = frame[y1:y2, x1:x2]
                            save_path = os.path.join(violation_dir, f"{int(tracker_id)}_{int(speed)}.jpg")
                            cv2.imwrite(save_path, cropped)

                            processed_violators.append({
                                "tracker_id": int(tracker_id),
                                "speed": round(speed, 2),
                                "image": save_path
                            })
                            colors.append((0, 0, 255))
                        else:
                            colors.append((0, 255, 0))

                annotated_frame = frame.copy()
                annotated_frame = trace_annotator.annotate(annotated_frame, detections)
                annotated_frame = box_annotator.annotate(annotated_frame, detections=detections)
                annotated_frame = label_annotator.annotate(annotated_frame, detections, labels=labels)

                cv2.polylines(annotated_frame, [SOURCE.reshape((-1, 1, 2))], True, (0, 255, 255), 2)
                sink.write_frame(annotated_frame)

        return JSONResponse(content={
            "violators": processed_violators,
            "vehicles": processed_vehicles,
            "output_video": output_path
        })
    
    except Exception as e:
        if os.path.exists(input_path):
            os.remove(input_path)
        return JSONResponse(
            status_code=500,
            content={"error": str(e)}
        )
    
    finally:
        if os.path.exists(input_path):
            os.remove(input_path)