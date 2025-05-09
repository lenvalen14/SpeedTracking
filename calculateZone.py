import cv2

# Lưu điểm được chọn
points = []
paused = False

def click_event(event, x, y, flags, param):
    global points, frame
    if event == cv2.EVENT_LBUTTONDOWN and len(points) < 4:
        points.append((x, y))
        print(f"Point {len(points)}: ({x}, {y})")
        cv2.circle(frame, (x, y), 5, (0, 0, 255), -1)
        if len(points) > 1:
            cv2.line(frame, points[-2], points[-1], (255, 0, 0), 2)
        cv2.imshow("Video", frame)

# ==== Đường dẫn video ====
VIDEO_PATH = 'video.mp4'  # đổi thành tên video của bạn
cap = cv2.VideoCapture(VIDEO_PATH)

cv2.namedWindow("Video")
cv2.setMouseCallback("Video", click_event)

print("🔹 Nhấn 'p' để tạm dừng video, sau đó click 4 điểm.")
print("🔹 Nhấn ESC để thoát.")

while cap.isOpened():
    if not paused:
        ret, frame = cap.read()
        if not ret:
            break
        cv2.imshow("Video", frame)

    key = cv2.waitKey(30) & 0xFF
    if key == ord('p'):
        paused = not paused
    elif key == 27:  # ESC
        break

cap.release()
cv2.destroyAllWindows()

# In mảng zone nếu đủ điểm
if len(points) == 4:
    print("\n✅ SOURCE zone:")
    print("SOURCE = np.array([")
    for pt in points:
        print(f"    [{pt[0]}, {pt[1]}],")
    print("])")
else:
    print("⚠️ Chưa chọn đủ 4 điểm.")
