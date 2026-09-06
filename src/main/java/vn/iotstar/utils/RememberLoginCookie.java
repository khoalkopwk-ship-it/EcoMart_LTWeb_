package vn.iotstar.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Quản lý cookie chỉ dùng để ghi nhớ định danh của lần đăng nhập gần nhất trong 24 giờ. */
public final class RememberLoginCookie {
    public static final String NAME = "ECOMART_LAST_LOGIN";
    public static final int MAX_AGE_SECONDS = 24 * 60 * 60;

    private RememberLoginCookie() { }

    /** Tạo lại cookie sau login; cookie chỉ chứa username mã hóa Base64 URL và không dùng để xác thực. */
    public static void refresh(HttpServletRequest request, HttpServletResponse response, String username) {
        String encoded = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(username.getBytes(StandardCharsets.UTF_8));
        Cookie cookie = new Cookie(NAME, encoded);
        cookie.setPath(cookiePath(request));
        cookie.setMaxAge(MAX_AGE_SECONDS);
        cookie.setHttpOnly(true);
        cookie.setSecure(request.isSecure());
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);
    }

    /** Đọc username để điền form login; dữ liệu hỏng bị bỏ qua và không bao giờ tạo session xác thực. */
    public static String read(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (!NAME.equals(cookie.getName())) continue;
            try {
                String decoded = new String(Base64.getUrlDecoder().decode(cookie.getValue()), StandardCharsets.UTF_8);
                return FormValidationUtil.isValidUsername(decoded) ? decoded : null;
            } catch (IllegalArgumentException exception) {
                return null;
            }
        }
        return null;
    }

    /** Giới hạn cookie trong context hiện tại hoặc toàn site nếu ứng dụng chạy ở context gốc. */
    private static String cookiePath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return contextPath == null || contextPath.isBlank() ? "/" : contextPath;
    }
}
