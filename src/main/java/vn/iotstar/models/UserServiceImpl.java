package vn.iotstar.models;

import vn.iotstar.entity.User;
import vn.iotstar.entity.Role;
import vn.iotstar.config.FixedAdminConfig;
import vn.iotstar.repository.IUserRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.utils.FormValidationUtil;
import vn.iotstar.utils.PasswordUtil;

public class UserServiceImpl implements UserService {
    private final IUserRepository repository;
    /**
     * Khởi tạo service với repository mặc định để controller sử dụng; chuyển việc gán phụ thuộc sang
     * constructor nhận repository.
     */
    public UserServiceImpl() { this(new UserRepository()); }
    /**
     * Nhận repository từ bên ngoài và lưu làm phụ thuộc cho các hàm nghiệp vụ; cho phép thay thế nguồn truy
     * cập dữ liệu khi kiểm thử.
     */
    public UserServiceImpl(IUserRepository repository) { this.repository = repository; }

    /**
     * Được VerifyOtpController gọi sau khi OTP hợp lệ. Kiểm tra email, username, họ tên và mật khẩu bằng các
     * giới hạn của form, đồng thời dò trùng qua existsEmail/existsUsername; chuẩn hóa dữ liệu, băm mật khẩu,
     * đặt status = 1 rồi repository.insert.
     */
    @Override public void register(User user) {
        if (user == null || !FormValidationUtil.isValidEmail(user.getEmail())
                || !FormValidationUtil.isValidUsername(user.getUsername())
                || user.getFullname() == null || user.getFullname().isBlank()
                || user.getFullname().trim().length() > 255
                || !FormValidationUtil.isValidPassword(user.getPassword())) {
            throw new IllegalArgumentException("Thông tin đăng ký không hợp lệ");
        }
        if (existsEmail(user.getEmail())) throw new IllegalArgumentException("Email đã được sử dụng");
        if (existsUsername(user.getUsername())) throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
        if (FixedAdminConfig.username().equalsIgnoreCase(user.getUsername().trim())
                || FixedAdminConfig.email().equalsIgnoreCase(user.getEmail().trim())) {
            throw new IllegalArgumentException("Tên đăng nhập hoặc email này được dành cho quản trị viên");
        }
        user.setEmail(user.getEmail().trim().toLowerCase());
        user.setUsername(user.getUsername().trim());
        user.setFullname(user.getFullname().trim());
        user.setPassword(PasswordUtil.hash(user.getPassword()));
        user.setStatus(1);
        user.setRole(Role.USER);
        repository.insert(user);
    }
    /**
     * Được LoginController gọi với username/email và mật khẩu thô. Tìm user bằng repository.findByLogin, yêu
     * cầu status = 1 và PasswordUtil.verify thành công; trả User để controller lưu session.account, hoặc null
     * khi xác thực thất bại.
     */
    @Override public User login(String login, String password) {
        if (login == null || login.isBlank() || login.trim().length() > 255
                || password == null || password.isEmpty() || password.length() > 255) return null;
        User user = repository.findByLogin(login.trim());
        if (user == null || user.getStatus() != 1 || !PasswordUtil.verify(password, user.getPassword())) return null;
        return user;
    }
    /**
     * Kiểm tra email khác null rồi gọi repository.findByEmail với chuỗi đã trim; trả boolean cho luồng đăng ký
     * và quên mật khẩu.
     */
    @Override public boolean existsEmail(String email) {
        return FormValidationUtil.isValidEmail(email)
                && repository.findByEmail(email.trim().toLowerCase()) != null;
    }
    /**
     * Kiểm tra username khác null rồi gọi repository.findByUsername với chuỗi đã trim; trả boolean để luồng
     * đăng ký chặn tên tài khoản trùng.
     */
    @Override public boolean existsUsername(String username) {
        return FormValidationUtil.isValidUsername(username)
                && repository.findByUsername(username.trim()) != null;
    }
    /**
     * Yêu cầu mật khẩu mới tối thiểu 6 ký tự, băm bằng PasswordUtil.hash rồi gọi repository.updatePassword
     * theo email. ResetPasswordController chịu trách nhiệm kiểm tra cờ xác minh OTP trước khi gọi hàm này.
     */
    @Override public void resetPassword(String email, String newPassword) {
        if (!FormValidationUtil.isValidPassword(newPassword))
            throw new IllegalArgumentException("Mật khẩu phải từ 6 đến 255 ký tự");
        repository.updatePassword(email, PasswordUtil.hash(newPassword));
    }
    //Profile
    /**
     * Kiểm tra họ tên bắt buộc, trim và giới hạn 255 ký tự; chuẩn hóa số điện thoại, cho phép trống hoặc chuỗi
     * 8–20 ký tự thuộc mẫu quy định. Gọi repository.updateProfile và trả User cập nhật để ProfileController
     * thay session.account; lỗi dữ liệu ném IllegalArgumentException.
     */
    @Override
    public User updateProfile(
            int userId,
            String fullname,
            String phone,
            String images) {

        if (fullname == null || fullname.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Họ và tên không được để trống"
            );
        }

        fullname = fullname.trim();

        if (fullname.length() > 255) {
            throw new IllegalArgumentException(
                    "Họ và tên không được vượt quá 255 ký tự"
            );
        }

        phone = phone == null ? "" : phone.trim();

        if (!phone.isEmpty()
                && !phone.matches("[0-9+() .-]{8,20}")) {
            throw new IllegalArgumentException(
                    "Số điện thoại không hợp lệ"
            );
        }

        return repository.updateProfile(
                userId,
                fullname,
                phone,
                images
        );
    }
    /**
     * Trả null nếu id không dương; còn lại gọi repository.findById. ProfileController dùng để làm mới hồ sơ và
     * phát hiện tài khoản đã bị xóa.
     */
    @Override
    public User findById(int id) {
        if (id <= 0) {
            return null;
        }

        return repository.findById(id);
    }

    /**
     * Bảo đảm tài khoản ADMIN cố định tồn tại. Nếu đã có đúng username/email thì đồng bộ mật khẩu và role;
     * nếu hai định danh bị chiếm bởi các tài khoản khác nhau thì dừng để tránh cấp nhầm quyền.
     */
    @Override
    public void ensureFixedAdmin() {
        User byUsername = repository.findByUsername(FixedAdminConfig.username());
        User byEmail = repository.findByEmail(FixedAdminConfig.email());
        if (byUsername != null && byEmail != null && byUsername.getId() != byEmail.getId()) {
            throw new IllegalStateException("Username và email ADMIN đang thuộc hai tài khoản khác nhau");
        }
        User admin = byUsername != null ? byUsername : byEmail;
        if (admin == null) {
            admin = new User();
            admin.setUsername(FixedAdminConfig.username());
            admin.setEmail(FixedAdminConfig.email().toLowerCase());
            admin.setFullname(FixedAdminConfig.fullname());
            admin.setPassword(PasswordUtil.hash(FixedAdminConfig.password()));
            admin.setStatus(1);
            admin.setRole(Role.ADMIN);
            repository.insert(admin);
            return;
        }
        repository.updateFixedAdmin(admin.getId(), FixedAdminConfig.username(),
                FixedAdminConfig.email().toLowerCase(), FixedAdminConfig.fullname(),
                PasswordUtil.hash(FixedAdminConfig.password()));
    }
}
