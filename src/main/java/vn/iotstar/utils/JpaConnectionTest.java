package vn.iotstar.utils;

import jakarta.persistence.EntityManager;

/**
 * Chạy trực tiếp class này trong IntelliJ để kiểm tra persistence.xml và SQL Server.
 */
public final class JpaConnectionTest {

    private JpaConnectionTest() {
    }

    public static void main(String[] args) {
        try (EntityManager entityManager = JpaConfig.getEntityManager()) {
            Object result = entityManager.createNativeQuery("SELECT 1", Integer.class)
                    .getSingleResult();
            long categoryCount = entityManager
                    .createQuery("SELECT COUNT(c) FROM Category c", Long.class)
                    .getSingleResult();
            long productCount = entityManager
                    .createQuery("SELECT COUNT(p) FROM Product p", Long.class)
                    .getSingleResult();
            long userCount = entityManager
                    .createQuery("SELECT COUNT(u) FROM User u", Long.class)
                    .getSingleResult();

            System.out.println("KẾT NỐI JPA THÀNH CÔNG");
            System.out.println("SELECT 1 = " + result);
            System.out.println("Số Category hiện có = " + categoryCount);
            System.out.println("Số Product hiện có = " + productCount);
            System.out.println("Số User hiện có = " + userCount);
        } catch (Exception exception) {
            System.err.println("KẾT NỐI JPA THẤT BẠI");
            exception.printStackTrace();
            System.exit(1);
        } finally {
            JpaConfig.close();
        }
    }
}
