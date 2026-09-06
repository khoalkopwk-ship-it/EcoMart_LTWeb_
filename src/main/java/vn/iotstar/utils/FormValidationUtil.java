package vn.iotstar.utils;

import java.util.regex.Pattern;

/** Các quy tắc validation dùng chung giữa controller và service của những biểu mẫu tài khoản. */
public final class FormValidationUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,63}$",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9._-]{3,100}$");
    private static final Pattern OTP_PATTERN = Pattern.compile("^[0-9]{6}$");

    /** Constructor private ngăn tạo đối tượng; các quy tắc được gọi trực tiếp bằng phương thức static. */
    private FormValidationUtil() {
    }

    /**
     * Trả true khi email có tối đa 255 ký tự và đúng cấu trúc tên@miền.phần-mở-rộng. Controller dùng để báo
     * lỗi sớm; UserService dùng lại trước khi truy vấn hoặc ghi dữ liệu.
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        String normalized = email.trim();
        return !normalized.isEmpty()
                && normalized.length() <= 255
                && EMAIL_PATTERN.matcher(normalized).matches();
    }

    /**
     * Trả true khi username dài 3–100 ký tự và chỉ gồm chữ Latin, số, dấu chấm, gạch dưới hoặc gạch ngang;
     * RegisterController và UserService cùng gọi để frontend và backend áp dụng một quy tắc.
     */
    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username.trim()).matches();
    }

    /**
     * Trả true khi mật khẩu dài từ 6 đến 255 ký tự. RegisterController, ResetPasswordController và
     * UserService gọi trước khi băm để chặn dữ liệu thiếu hoặc quá dài.
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6 && password.length() <= 255;
    }

    /**
     * Trả true khi OTP sau trim có đúng 6 chữ số. Hai controller xác minh gọi trước khi so sánh với mã trong
     * session, đồng bộ với pattern ở biểu mẫu HTML.
     */
    public static boolean isValidOtp(String otp) {
        return otp != null && OTP_PATTERN.matcher(otp.trim()).matches();
    }
}
