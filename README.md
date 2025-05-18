
# 🚓 Hệ Thống Nhận Diện Tốc Độ Phương Tiện (VehicleSpeedDetection)

## Giới Thiệu

**Hệ thống Nhận diện Tốc độ Phương tiện** là một giải pháp ứng dụng thị giác máy tính và trí tuệ nhân tạo nhằm phát hiện phương tiện giao thông và tính toán tốc độ di chuyển của chúng thông qua video giám sát.  
Hệ thống hỗ trợ phát hiện vi phạm tốc độ và lưu trữ thông tin để phục vụ công tác quản lý giao thông.

Dự án bao gồm 3 thành phần chính: **AI**, **Backend**, và **Frontend**, với khả năng xử lý video theo thời gian thực và cảnh báo phương tiện vi phạm.

---

## 🔍 Tính Năng Chính

### 1. Phát Hiện & Theo Dõi Phương Tiện

- Phát hiện xe máy, ô tô, xe tải trong video từ camera giám sát  
- Theo dõi chuyển động của từng phương tiện qua nhiều khung hình (tracking)  
- Gán ID cho từng xe để tính tốc độ chính xác  

### 2. Tính Toán & Cảnh Báo Tốc Độ

- Tính tốc độ phương tiện dựa trên khoảng cách pixel và thời gian  
- Cảnh báo khi vượt quá ngưỡng tốc độ cấu hình  
- Ghi nhận vi phạm kèm ảnh, thời gian, vị trí  

### 3. Giao Diện Bản Đồ & Quản Lý Dữ Liệu

- Hiển thị video giám sát có gắn nhãn tốc độ từng xe  
- Danh sách các xe vi phạm tốc độ với thông tin chi tiết  
- Bản đồ hiển thị vị trí camera và vùng giám sát  

---

## 📷 Minh Họa Giao Diện

**Giao diện Dashboard của hệ thống**  
![Dashboard](https://github.com/user-attachments/assets/6352f4fb-db6c-404d-aa7f-499620af10d8)

**Giao diện tương tác biểu đồ giao thông của hệ thống**  
![Charts](https://github.com/user-attachments/assets/027b0ed7-8315-4cc9-9936-5dd267f1f038)

**Giao diện camera gần đây của hệ thống**  
![Recent Cameras](https://github.com/user-attachments/assets/3f932451-24ff-41d2-8bba-ea4f42ebfc8c)

**Giao diện Violations của hệ thống**  
![Violations](https://github.com/user-attachments/assets/7141c019-42e9-4f17-85f1-ac088e59f66d)

**Giao diện Settings của hệ thống**  
![Settings](https://github.com/user-attachments/assets/6e17c02a-134a-416d-ad7a-78cb74873961)

**Giao diện Video camera chi tiết của hệ thống**  
![Camera View](https://github.com/user-attachments/assets/a5cabe62-5ec1-4c81-823f-63533585c9ec)

---

## ⚙️ Công Nghệ Sử Dụng

| Thành phần     | Công nghệ                            |
|----------------|--------------------------------------|
| **AI & Vision**| Python, OpenCV, YOLOv8, ByteTrack, paddleOCR |
| **Backend**    | FastAPI, Java Spring Boot            |
| **Frontend**   | NextJS, Typescript                   |
| **Database**   | MongoDB                              |

---

## 📁 Cấu Trúc Dự Án

```
SpeedTracking/
├── AI/                 # Thành phần AI
│   ├── models/         # Các mô hình đã huấn luyện
│   ├── src/            # Mã nguồn AI
│   └── requirements.txt
├── BE/                 # Backend
│   ├── src/            # Mã nguồn BE
│   ├── pom.xml         # Cấu hình Maven
│   └── README.md
└── FE/                 # Frontend
    ├── src/            # Mã nguồn FE
    ├── public/         # Tài nguyên công khai
    └── package.json
```
