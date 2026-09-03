# ServletCRUDMVC-JPA-EcoMart

Dự án hoàn thiện bài tập Servlet MVC/JPA trên nền `ServletCRUDMVC-JPA`, chuyển các luồng tương tự và phong cách giao diện từ EcoMarts sang cấu trúc `vn.iotstar.*`. Những phần EcoMarts không cung cấp đúng yêu cầu JPA được hoàn thiện theo công nghệ lõi của BTWEB.

## 1. Công nghệ

- Java 17, Maven WAR, Tomcat 11.
- Jakarta Servlet 6.1, JSP, JSTL 3.
- Jakarta Persistence/JPA và Hibernate ORM.
- Microsoft SQL Server và Microsoft JDBC Driver.
- Jakarta Mail/Angus Mail để gửi OTP bằng Gmail SMTP.
- Multipart để upload icon Category và ảnh Product.
- PBKDF2-HMAC-SHA256 để băm mật khẩu.
- Bootstrap được thay bằng bộ CSS EcoMart thích ứng màn hình trong `assets/css`.

## 2. Kiến trúc giữ theo dự án nền

```text
JSP
  → Controller (vn.iotstar.controllers)
  → Service (vn.iotstar.models)
  → Repository (vn.iotstar.repository)
  → EntityManager/JPA/Hibernate
  → SQL Server
```

`JpaConfig` và persistence unit `dataSource` của dự án nền được giữ nguyên cách hoạt động. Ba biến `DB_URL`, `DB_USER`, `DB_PASSWORD` có thể ghi đè cấu hình mặc định.

## 3. Chức năng hoàn thiện

| Yêu cầu | URL | Thành phần chính |
| --- | --- | --- |
| Đăng ký và gửi OTP email | `/register` | `RegisterController`, `EmailUtil`, `OtpUtil` |
| Xác nhận OTP, tạo tài khoản | `/verify-otp` | `VerifyOtpController`, `UserService` |
| Đăng nhập bằng username/email | `/login` | `LoginController`, `PasswordUtil` |
| Quên mật khẩu và gửi OTP | `/forgot-password` | `ForgotPasswordController` |
| Xác nhận OTP quên mật khẩu | `/forgot-password/verify` | `VerifyForgotOtpController` |
| Đặt mật khẩu mới | `/reset-password` | `ResetPasswordController` |
| Category CRUD + Multipart | `/admin/category/list` | Controller/Service/Repository Category |
| Product CRUD + Multipart | `/admin/product/list` | Controller/Service/Repository Product |
| 10 sản phẩm mới nhất | `/home` | `findTop10Newest()` |
| Tất cả sản phẩm phân trang | `/product?page=1` | `setFirstResult`, `setMaxResults` |
| Chi tiết sản phẩm | `/product/detail?id=1` | `ProductDetailController` |

## 4. Nguồn tham chiếu và cách điều chỉnh

### Từ EcoMarts

- Luồng đăng ký → email OTP → xác nhận tài khoản.
- Luồng đăng nhập và khôi phục mật khẩu.
- Các trang danh sách/chi tiết sản phẩm, trang quản trị và form Multipart.
- Hệ thống thiết kế màu be–xanh lá, logo, banner, header, footer, sidebar, card và bảng quản trị.

Code EcoMarts dùng JDBC, tên package và mô hình dữ liệu khác nên không chép nguyên. Các luồng được viết lại bằng Entity/Repository/Service của dự án này để tương thích JPA và Tomcat 11.

### Từ BTWEB

- Entity Product quan hệ nhiều-một với Category.
- JPQL CRUD Product.
- Truy vấn 10 sản phẩm mới nhất.
- Phân trang phía database.
- OTP 5 phút cho quên mật khẩu.

Các lỗi của bản tham chiếu đã được tránh: Product có form Create thật, upload ảnh Product dùng Multipart, đường dẫn JSP đúng và mật khẩu không lưu nguyên văn.

## 5. Chuẩn bị SQL Server

1. Mở SQL Server Management Studio.
2. Chạy toàn bộ `database.sql`.
3. Script tạo/hoàn thiện database `BT02_CRUD_JPA`, ba bảng `Category`, `Products`, `Users`, khóa ngoại và 20 sản phẩm mẫu.
4. Đặt biến môi trường kết nối SQL Server, đặc biệt `DB_PASSWORD` vì source không chứa mật khẩu thật:

```text
DB_URL=jdbc:sqlserver://localhost:1433;databaseName=BT02_CRUD_JPA;encrypt=true;trustServerCertificate=true
DB_USER=sa
DB_PASSWORD=mat_khau_sql_server
```

Nếu dùng instance SQLEXPRESS, có thể đổi `DB_URL` thành:

```text
jdbc:sqlserver://localhost;instanceName=SQLEXPRESS01;databaseName=BT02_CRUD_JPA;encrypt=true;trustServerCertificate=true
```

## 6. Cấu hình gửi OTP

Tạo Gmail App Password rồi đặt hai biến môi trường trong cấu hình chạy Tomcat/SmartTomcat:

```text
MAIL_USERNAME=email_he_thong@gmail.com
MAIL_PASSWORD=mat_khau_ung_dung_16_ky_tu
```

Không dùng mật khẩu đăng nhập Gmail thông thường.

## 7. Chạy bằng IntelliJ và Tomcat 11

1. Mở thư mục dự án bằng IntelliJ.
2. Chọn Maven Reload Project.
3. Chọn Project SDK JDK 17 trở lên; source release vẫn là 17 để ổn định.
4. Thêm Tomcat 11 hoặc SmartTomcat.
5. Deployment artifact: `ServletCRUDMVC-JPA-EcoMart:war exploded`.
6. Context path đề xuất: `/ServletCRUDMVC-JPA-EcoMart`.
7. Truy cập `http://localhost:8080/ServletCRUDMVC-JPA-EcoMart/`.

## 8. Lưu ảnh

- Ảnh có sẵn nằm trong `src/main/webapp/images/category` và `images/product`.
- Ảnh upload được lưu vào thư mục triển khai tương ứng của Tomcat.
- Database chỉ lưu đường dẫn tương đối như `product/ao-polo-nam.png`.
- Servlet `/image?fname=...` kiểm tra đường dẫn chuẩn hóa để ngăn đọc file bên ngoài thư mục ảnh.

## 9. Lưu ý

- Bài tập không yêu cầu phân quyền nên các URL quản trị không kiểm tra role.
- Xóa Product/Category dùng POST. Category đang có Product sẽ không bị xóa và giao diện hiển thị cảnh báo.
- Tài khoản chỉ được ghi vào database sau khi OTP đăng ký hợp lệ.
