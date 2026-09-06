package vn.iotstar.utils;

import java.security.SecureRandom;

public final class OtpUtil {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final long VALID_TIME_MILLIS = 5 * 60 * 1000L;
    /**
     * Constructor private ngăn tạo đối tượng; các chức năng của lớp tiện ích được gọi trực tiếp qua phương
     * thức static.
     */
    private OtpUtil() { }
    /**
     * Sinh chuỗi OTP ngẫu nhiên 6 chữ số bằng SecureRandom; controller đăng ký/quên mật khẩu gửi qua EmailUtil
     * và lưu mã cùng thời điểm tạo trong session để xác minh sau.
     */
    public static String generate() { return String.valueOf(100000 + RANDOM.nextInt(900000)); }
    /**
     * So sánh thời gian hiện tại với createdTime theo mili giây; trả true khi đã quá 5 phút. Các controller
     * xác minh OTP gọi trước khi đối chiếu mã trong session.
     */
    public static boolean isExpired(long createdTime) {
        return System.currentTimeMillis() - createdTime > VALID_TIME_MILLIS;
    }
}
