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

    private JpaConfig() {
    }

    public static EntityManager getEntityManager() {
        if (!FACTORY.isOpen()) {
            throw new IllegalStateException("EntityManagerFactory đã đóng");
        }
        return FACTORY.createEntityManager();
    }

    public static boolean isOpen() {
        return FACTORY.isOpen();
    }

    public static void close() {
        if (FACTORY.isOpen()) {
            FACTORY.close();
        }
    }

    private static EntityManagerFactory createFactory() {
        Map<String, Object> overrides = new HashMap<>();
        copyEnvironmentVariable(overrides, "DB_URL", "jakarta.persistence.jdbc.url");
        copyEnvironmentVariable(overrides, "DB_USER", "jakarta.persistence.jdbc.user");
        copyEnvironmentVariable(overrides, "DB_PASSWORD", "jakarta.persistence.jdbc.password");
        return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, overrides);
    }

    private static void copyEnvironmentVariable(
            Map<String, Object> properties, String environmentName, String jpaName) {
        String value = System.getenv(environmentName);
        if (value != null && !value.isBlank()) {
            properties.put(jpaName, value);
        }
    }
}
