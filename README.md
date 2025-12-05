# Spring Boot Video Management System

Dự án quản lý Video/Category/User được nâng cấp từ Servlet/JSP sang **Spring Boot 3 + Thymeleaf**, tích hợp **RESTful API** và **AJAX**.

## Các thay đổi chính (Key Changes)

- **Công nghệ:** Chuyển đổi từ JSP/SiteMesh sang **Thymeleaf Layout Dialect**.
- **Backend:** Nâng cấp lên **Spring Boot 3.3.5**.
- **Database:** Sử dụng Spring Data JPA (Hibernate) thay vì JDBC thuần.
- **API:** Tích hợp **Swagger 3 (OpenAPI)** để tài liệu hóa API.
- **Frontend:**
  - **Thymeleaf:** CRUD + Tìm kiếm + Phân trang (Server-side rendering).
  - **AJAX/jQuery:** CRUD + Tìm kiếm + Phân trang không tải lại trang (Client-side rendering) cho module Video.

## 🛠 Cài đặt & Cấu hình

1. **Database:**
   - Tạo database tên `web_springmvc` (hoặc tên trong `application.properties`).
   - Cập nhật username/password trong `src/main/resources/application.properties`.
2. **Uploads:**
   - Dự án tự động cấu hình lưu ảnh vào `src/main/resources/static/uploads`.
3. **Chạy ứng dụng:**
   - Chạy file `SpringbootsTymeleafApplication.java`.
   - Truy cập: `http://localhost:8088`.

## Hướng dẫn kiểm tra (Testing Guide)

### 1. Đăng nhập (Admin Role)
Sử dụng tài khoản Admin đã seed trong database để truy cập Dashboard:
- **URL:** `http://localhost:8088/auth/login`
- **User:** `admin` (hoặc username bạn đã tạo)
- **Pass:** `123456` (lưu ý mật khẩu phải được mã hóa BCrypt trong DB hoặc tạo qua trang Register).

### 2. Kiểm tra Search & Pagination (Thymeleaf Version)
Áp dụng cho module **Users** và **Categories**.
- **Truy cập:** `http://localhost:8088/admin/categories`
- **Tính năng:**
  - Nhập từ khóa vào ô Search -> Nhấn Tìm kiếm (URL thay đổi kèm param `?name=...`).
  - Chuyển trang (Pagination) ở footer bảng dữ liệu.
  - CRUD (Thêm, Sửa, Xóa) load lại trang theo cơ chế truyền thống.

### 3. Kiểm tra AJAX CRUD (Tính năng mới) 
Đây là yêu cầu nâng cao áp dụng cho module **Videos**. Giao diện không load lại trang.
- **Truy cập:** `http://localhost:8088/admin/videos/ajax`
- **Cách kiểm tra:**
  1. Mở **F12** -> Tab **Network** -> Chọn filter **Fetch/XHR**.
  2. **Tìm kiếm:** Nhập tên video -> Bấm "Tìm". Quan sát trang không reload, chỉ có request ngầm gửi đi.
  3. **Phân trang:** Bấm số trang 1, 2... -> Bảng dữ liệu tự cập nhật.
  4. **Thêm/Sửa:** Bấm nút Thêm/Sửa -> Modal hiện ra -> Lưu lại -> Dữ liệu cập nhật ngay lập tức mà không reload.

### 4. Kiểm tra API Document (Swagger 3)
Xem danh sách toàn bộ API đã viết cho AJAX.
- **URL:** `http://localhost:8088/swagger-ui/index.html`
- **Nhóm API:** `video-api-controller`, `category-api-controller`.

## Cấu trúc Source Code

- `src/main/java/com/hoainam/controller/admin/*`: Controller xử lý Thymeleaf.
- `src/main/java/com/hoainam/controller/api/*`: RestController xử lý JSON cho AJAX.
- `src/main/resources/templates/admin/*/searchpaging.html`: View Thymeleaf chuẩn.
- `src/main/resources/templates/admin/video-ajax.html`: View sử dụng jQuery AJAX.

---
**Sinh viên thực hiện:** Phạm Hoài Nam
**MSSV:** 23110127
