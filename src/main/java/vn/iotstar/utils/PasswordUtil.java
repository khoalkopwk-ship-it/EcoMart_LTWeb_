package vn.iotstar.utils;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordUtil {
    private static final int ITERATIONS = 65_536;
    private static final int KEY_LENGTH = 256;
    private static final SecureRandom RANDOM = new SecureRandom();
    /**
     * Constructor private ngăn tạo đối tượng; các chức năng của lớp tiện ích được gọi trực tiếp qua phương
     * thức static.
     */
    private PasswordUtil() { }

    /**
     * Sinh salt ngẫu nhiên 16 byte, gọi pbkdf2 với 65.536 vòng và đóng gói thành
     * iterations:saltBase64:hashBase64. UserService gọi khi đăng ký/đổi mật khẩu để lưu chuỗi băm, còn verify
     * đọc lại định dạng này khi đăng nhập.
     */
    public static String hash(String rawPassword) {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        byte[] hash = pbkdf2(rawPassword.toCharArray(), salt, ITERATIONS);
        return ITERATIONS + ":" + Base64.getEncoder().encodeToString(salt)
                + ":" + Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Đọc số vòng, salt và hash từ encodedPassword, gọi pbkdf2 với mật khẩu nhập rồi so sánh bằng
     * MessageDigest.isEqual. UserService.login dùng kết quả xác thực; trả false khi đầu vào null, sai cấu trúc
     * hoặc phát sinh IllegalArgumentException lúc giải mã/tính toán.
     */
    public static boolean verify(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) return false;
        String[] parts = encodedPassword.split(":");
        if (parts.length != 3) return false;
        try {
            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = Base64.getDecoder().decode(parts[1]);
            byte[] expected = Base64.getDecoder().decode(parts[2]);
            byte[] actual = pbkdf2(rawPassword.toCharArray(), salt, iterations);
            return MessageDigest.isEqual(expected, actual);
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    /**
     * Hàm chung cho hash/verify: tạo khóa 256 bit bằng PBKDF2WithHmacSHA256 với mật khẩu, salt và số vòng được
     * truyền vào. Chuyển lỗi thuật toán mật mã thành IllegalStateException để bên gọi nhận lỗi hệ thống.
     */
    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, KEY_LENGTH);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Không thể mã hóa mật khẩu", exception);
        }
    }
}
