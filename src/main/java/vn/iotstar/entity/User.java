package vn.iotstar.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
    @Column(nullable = false, unique = true, length = 255) private String email;
    @Column(nullable = false, unique = true, length = 100) private String username;
    @Column(nullable = false, length = 255) private String fullname;
    @Column(nullable = false, length = 512) private String password;
    @Column(nullable = false) private int status;
    @Column(name = "createdDate", nullable = false) private LocalDateTime createdDate;
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Role role = Role.USER;
    // Profile
    @Column(length = 20) private String phone;
    @Column(length = 255) private String images;

    /**
     * Constructor không tham số để JPA tạo entity khi đọc dữ liệu; controller cũng có thể tạo đối tượng rỗng
     * rồi điền dữ liệu bằng setter.
     */
    public User() { }
    /**
     * Callback @PrePersist do JPA gọi trước khi insert: gán thời gian hiện tại nếu createdDate chưa có, giữ
     * giá trị đã đặt trước. Repository kích hoạt gián tiếp khi persist entity.
     */
    @PrePersist public void prePersist() { if (createdDate == null) createdDate = LocalDateTime.now(); }
    /**
     * Trả mã định danh của User cho tầng xử lý hoặc JSP đọc qua thuộc tính JavaBean; getter chỉ đọc dữ liệu
     * đang có trên entity.
     */
    public int getId() { return id; }
    /**
     * Gán mã định danh vào User trong bộ nhớ để controller/service chuẩn bị dữ liệu; việc lưu xuống cơ sở dữ
     * liệu do repository và vòng đời JPA thực hiện.
     */
    public void setId(int id) { this.id = id; }
    /**
     * Trả email của User cho tầng xử lý hoặc JSP đọc qua thuộc tính JavaBean; getter chỉ đọc dữ liệu đang có
     * trên entity.
     */
    public String getEmail() { return email; }
    /**
     * Gán email vào User trong bộ nhớ để controller/service chuẩn bị dữ liệu; việc lưu xuống cơ sở dữ liệu do
     * repository và vòng đời JPA thực hiện.
     */
    public void setEmail(String email) { this.email = email; }
    /**
     * Trả tên đăng nhập của User cho tầng xử lý hoặc JSP đọc qua thuộc tính JavaBean; getter chỉ đọc dữ liệu
     * đang có trên entity.
     */
    public String getUsername() { return username; }
    /**
     * Gán tên đăng nhập vào User trong bộ nhớ để controller/service chuẩn bị dữ liệu; việc lưu xuống cơ sở dữ
     * liệu do repository và vòng đời JPA thực hiện.
     */
    public void setUsername(String username) { this.username = username; }
    /**
     * Trả họ tên của User cho tầng xử lý hoặc JSP đọc qua thuộc tính JavaBean; getter chỉ đọc dữ liệu đang có
     * trên entity.
     */
    public String getFullname() { return fullname; }
    /**
     * Gán họ tên vào User trong bộ nhớ để controller/service chuẩn bị dữ liệu; việc lưu xuống cơ sở dữ liệu do
     * repository và vòng đời JPA thực hiện.
     */
    public void setFullname(String fullname) { this.fullname = fullname; }
    /**
     * Trả giá trị mật khẩu hiện tại: User chờ đăng ký chứa mật khẩu thô, User lưu trong cơ sở dữ liệu chứa
     * chuỗi băm; UserService dùng khi băm hoặc xác minh.
     */
    public String getPassword() { return password; }
    /**
     * Gán giá trị mật khẩu vào entity; RegisterController đặt mật khẩu thô cho user chờ OTP, UserService thay
     * bằng chuỗi băm trước khi repository lưu. Setter không tự băm mật khẩu.
     */
    public void setPassword(String password) { this.password = password; }
    /**
     * Trả trạng thái của User cho tầng xử lý hoặc JSP đọc qua thuộc tính JavaBean; getter chỉ đọc dữ liệu đang
     * có trên entity.
     */
    public int getStatus() { return status; }
    /**
     * Gán trạng thái vào User trong bộ nhớ để controller/service chuẩn bị dữ liệu; việc lưu xuống cơ sở dữ
     * liệu do repository và vòng đời JPA thực hiện.
     */
    public void setStatus(int status) { this.status = status; }
    /**
     * Trả thời điểm tạo của User cho tầng xử lý hoặc JSP đọc qua thuộc tính JavaBean; getter chỉ đọc dữ liệu
     * đang có trên entity.
     */
    public LocalDateTime getCreatedDate() { return createdDate; }
    /**
     * Gán thời điểm tạo vào User trong bộ nhớ để controller/service chuẩn bị dữ liệu; việc lưu xuống cơ sở dữ
     * liệu do repository và vòng đời JPA thực hiện.
     */
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    //Profile
    /**
     * Trả số điện thoại của User cho tầng xử lý hoặc JSP đọc qua thuộc tính JavaBean; getter chỉ đọc dữ liệu
     * đang có trên entity.
     */
    public String getPhone() { return phone;}
    /**
     * Gán số điện thoại vào User trong bộ nhớ để controller/service chuẩn bị dữ liệu; việc lưu xuống cơ sở dữ
     * liệu do repository và vòng đời JPA thực hiện.
     */
    public void setPhone(String phone) {this.phone = phone;}
    /**
     * Trả đường dẫn ảnh đại diện của User cho tầng xử lý hoặc JSP đọc qua thuộc tính JavaBean; getter chỉ đọc
     * dữ liệu đang có trên entity.
     */
    public String getImages() {return images;}
    /**
     * Gán đường dẫn ảnh đại diện vào User trong bộ nhớ để controller/service chuẩn bị dữ liệu; việc lưu xuống
     * cơ sở dữ liệu do repository và vòng đời JPA thực hiện.
     */
    public void setImages(String images) {this.images = images;}

    /** Trả vai trò dùng bởi Filter và JSP; dữ liệu cũ chưa có role được xem là USER. */
    public Role getRole() { return role == null ? Role.USER : role; }

    /** Gán vai trò ở tầng service; form đăng ký không được phép gán trực tiếp ADMIN. */
    public void setRole(Role role) { this.role = role == null ? Role.USER : role; }

}
