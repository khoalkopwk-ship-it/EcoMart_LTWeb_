package vn.iotstar.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/** Tạo một EntityManagerFactory dùng chung trong suốt vòng đời ứng dụng. */
public final class JpaConfig {

    public static final String PERSISTENCE_UNIT = "dataSource";

    private static final EntityManagerFactory FACTORY = createFactory();

    /**
     * Constructor private ngăn tạo đối tượng; các chức năng của lớp tiện ích được gọi trực tiếp qua phương
     * thức static.
     */
    private JpaConfig() {
    }

    /**
     * Tạo EntityManager mới từ FACTORY dùng chung; repository dùng cho từng thao tác và chịu trách nhiệm đóng
     * sau khi xong. Nếu factory đã đóng thì ném IllegalStateException.
     */
    public static EntityManager getEntityManager() {
        if (!FACTORY.isOpen()) {
            throw new IllegalStateException("EntityManagerFactory đã đóng");
        }
        return FACTORY.createEntityManager();
    }

    /**
     * Trả trạng thái mở của FACTORY; JpaContextListener gọi khi ứng dụng khởi động, qua đó kích hoạt khởi tạo
     * tĩnh và createFactory nếu JpaConfig chưa được nạp.
     */
    public static boolean isOpen() {
        return FACTORY.isOpen();
    }

    /**
     * Đóng FACTORY nếu còn mở để giải phóng tài nguyên JPA; JpaContextListener gọi khi dừng ứng dụng.
     */
    public static void close() {
        if (FACTORY.isOpen()) {
            FACTORY.close();
        }
    }

    /**
     * Khởi tạo FACTORY dùng chung cho persistence unit dataSource. Gọi copyEnvironmentVariable để ghi đè
     * URL/user/password từ môi trường lên cấu hình persistence.xml rồi yêu cầu Persistence tạo
     * EntityManagerFactory.
     */
    private static EntityManagerFactory createFactory() {
        Map<String, Object> overrides = new HashMap<>();
        copyEnvironmentVariable(overrides, "DB_URL", "jakarta.persistence.jdbc.url");
        copyEnvironmentVariable(overrides, "DB_USER", "jakarta.persistence.jdbc.user");
        copyEnvironmentVariable(overrides, "DB_PASSWORD", "jakarta.persistence.jdbc.password");
        return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, overrides);
    }

    /**
     * Hàm phụ của createFactory: đọc biến môi trường theo environmentName, chỉ đưa vào map với khóa jpaName
     * nếu giá trị không null/trống; nhờ vậy cấu hình persistence.xml vẫn được dùng khi không có giá trị ghi
     * đè.
     */
    private static void copyEnvironmentVariable(
            Map<String, Object> properties, String environmentName, String jpaName) {
        String value = System.getenv(environmentName);
        if (value != null && !value.isBlank()) {
            properties.put(jpaName, value);
        }
    }
}
