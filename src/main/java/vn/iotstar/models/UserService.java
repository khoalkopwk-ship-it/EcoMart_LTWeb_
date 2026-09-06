package vn.iotstar.models;

import vn.iotstar.entity.User;

public interface UserService {
    /**
     * Hợp đồng service: Được VerifyOtpController gọi sau khi OTP đăng ký hợp lệ. Kiểm tra thông tin bắt buộc,
     * mật khẩu tối thiểu 6 ký tự và trùng email/username qua existsEmail/existsUsername; chuẩn hóa dữ liệu,
     * băm mật khẩu bằng PasswordUtil.hash, đặt status = 1 rồi repository.insert.
     */
    void register(User user);
    /**
     * Hợp đồng service: Được LoginController gọi với username/email và mật khẩu thô. Tìm user bằng
     * repository.findByLogin, yêu cầu status = 1 và PasswordUtil.verify thành công; trả User để controller lưu
     * session.account, hoặc null khi xác thực thất bại.
     */
    User login(String login, String password);
    /**
     * Hợp đồng service: Kiểm tra email khác null rồi gọi repository.findByEmail với chuỗi đã trim; trả boolean
     * cho luồng đăng ký và quên mật khẩu.
     */
    boolean existsEmail(String email);
    /**
     * Hợp đồng service: Kiểm tra username khác null rồi gọi repository.findByUsername với chuỗi đã trim; trả
     * boolean để luồng đăng ký chặn tên tài khoản trùng.
     */
    boolean existsUsername(String username);
    /**
     * Hợp đồng service: Yêu cầu mật khẩu mới tối thiểu 6 ký tự, băm bằng PasswordUtil.hash rồi gọi
     * repository.updatePassword theo email. ResetPasswordController chịu trách nhiệm kiểm tra cờ xác minh OTP
     * trước khi gọi hàm này.
     */
    void resetPassword(String email, String newPassword);
    /**
     * Hợp đồng service: Kiểm tra họ tên bắt buộc, trim và giới hạn 255 ký tự; chuẩn hóa số điện thoại, cho
     * phép trống hoặc chuỗi 8–20 ký tự thuộc mẫu quy định. Gọi repository.updateProfile và trả User cập nhật
     * để ProfileController thay session.account; lỗi dữ liệu ném IllegalArgumentException.
     */
    User updateProfile(
            int userId,
            String fullname,
            String phone,
            String images
    );
    /**
     * Hợp đồng service: Trả null nếu id không dương; còn lại gọi repository.findById. ProfileController dùng
     * để làm mới hồ sơ và phát hiện tài khoản đã bị xóa.
     */
    User findById(int id);

    /** Tạo hoặc đồng bộ duy nhất tài khoản ADMIN cố định từ cấu hình môi trường. */
    void ensureFixedAdmin();
}
