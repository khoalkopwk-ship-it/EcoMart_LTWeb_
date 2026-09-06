package vn.iotstar.config;

/** Đọc cấu hình tài khoản quản trị cố định từ biến môi trường. */
public final class FixedAdminConfig {
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_EMAIL = "admin@ecomart.local";
    private static final String DEFAULT_FULLNAME = "EcoMart Administrator";
    private static final String DEFAULT_PASSWORD = "Admin@123";

    private FixedAdminConfig() { }

    /** Trả username quản trị từ ADMIN_USERNAME hoặc giá trị mặc định. */
    public static String username() { return value("ADMIN_USERNAME", DEFAULT_USERNAME); }
    /** Trả email quản trị từ ADMIN_EMAIL hoặc giá trị mặc định. */
    public static String email() { return value("ADMIN_EMAIL", DEFAULT_EMAIL); }
    /** Trả họ tên quản trị từ ADMIN_FULLNAME hoặc giá trị mặc định. */
    public static String fullname() { return value("ADMIN_FULLNAME", DEFAULT_FULLNAME); }
    /** Trả mật khẩu quản trị từ ADMIN_PASSWORD hoặc giá trị mặc định. */
    public static String password() { return value("ADMIN_PASSWORD", DEFAULT_PASSWORD); }

    /** Đọc biến môi trường, loại khoảng trắng và dùng fallback khi biến chưa cấu hình. */
    private static String value(String name, String fallback) {
        String configured = System.getenv(name);
        return configured == null || configured.isBlank() ? fallback : configured.trim();
    }
}
