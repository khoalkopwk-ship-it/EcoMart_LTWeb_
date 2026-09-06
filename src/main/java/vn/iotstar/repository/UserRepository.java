package vn.iotstar.repository;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.iotstar.entity.User;
import vn.iotstar.entity.Role;
import vn.iotstar.utils.JpaConfig;

public class UserRepository implements IUserRepository {
    /**
     * Nhận User đã được service kiểm tra và băm mật khẩu; mở EntityManager qua JpaConfig, persist trong
     * transaction rồi commit. RuntimeException thì rollback nếu còn hoạt động và ném lại; luôn đóng
     * EntityManager.
     */
    @Override public void insert(User user) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin(); em.persist(user); transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction.isActive()) transaction.rollback();
            throw exception;
        } finally { em.close(); }
    }

    /**
     * Gọi single với JPQL so sánh email không phân biệt hoa/thường; trả User đầu tiên hoặc null để
     * UserService.existsEmail kiểm tra tồn tại.
     */
    @Override public User findByEmail(String email) {
        return single("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:value)", email);
    }
    /**
     * Gọi single với JPQL so sánh username không phân biệt hoa/thường; trả User đầu tiên hoặc null để
     * UserService.existsUsername kiểm tra trùng.
     */
    @Override public User findByUsername(String username) {
        return single("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:value)", username);
    }
    /**
     * Tìm tối đa một User có username hoặc email khớp không phân biệt hoa/thường; trả null nếu không có và
     * luôn đóng EntityManager. UserService.login tiếp tục kiểm tra trạng thái và mật khẩu.
     */
    @Override public User findByLogin(String login) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            List<User> users = em.createQuery(
                    "SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:login) OR LOWER(u.email) = LOWER(:login)",
                    User.class).setParameter("login", login).setMaxResults(1).getResultList();
            return users.isEmpty() ? null : users.get(0);
        } finally { em.close(); }
    }
    /**
     * Nhận mật khẩu đã băm từ UserService.resetPassword và chạy JPQL UPDATE theo email không phân biệt
     * hoa/thường trong transaction. Commit khi thành công, rollback khi lỗi runtime và luôn đóng
     * EntityManager; không kiểm tra số dòng được cập nhật.
     */
    @Override public void updatePassword(String email, String encodedPassword) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.createQuery("UPDATE User u SET u.password = :password WHERE LOWER(u.email) = LOWER(:email)")
                    .setParameter("password", encodedPassword)
                    .setParameter("email", email)
                    .executeUpdate();
            transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction.isActive()) transaction.rollback();
            throw exception;
        } finally { em.close(); }
    }

    /**
     * Hàm dùng chung của findByEmail/findByUsername: tạo truy vấn User từ JPQL, gắn tham số value và lấy tối
     * đa một kết quả. Trả null nếu rỗng và luôn đóng EntityManager.
     */
    private User single(String jpql, String value) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            List<User> users = em.createQuery(jpql, User.class)
                    .setParameter("value", value).setMaxResults(1).getResultList();
            return users.isEmpty() ? null : users.get(0);
        } finally { em.close(); }
    }
    // profile
    /**
     * Tìm User theo id trong transaction, báo IllegalArgumentException nếu không tồn tại. Cập nhật họ tên, đổi
     * điện thoại trống thành null và gán đường dẫn ảnh do service truyền vào; JPA ghi thay đổi khi commit. Trả
     * User để cập nhật session; rollback khi lỗi runtime và luôn đóng EntityManager.
     */
    @Override
    public User updateProfile(
            int id,
            String fullname,
            String phone,
            String images) {

        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            User user = em.find(User.class, id);

            if (user == null) {
                throw new IllegalArgumentException(
                        "Không tìm thấy tài khoản");
            }

            user.setFullname(fullname.trim());
            user.setPhone(
                    phone == null || phone.isBlank()
                            ? null
                            : phone.trim()
            );
            user.setImages(images);

            transaction.commit();
            return user;

        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;

        } finally {
            em.close();
        }
    }
    /**
     * Mở EntityManager qua JpaConfig và tìm User theo id; trả null nếu không tồn tại, luôn đóng EntityManager.
     * UserService.findById dùng để tải lại hồ sơ.
     */
    @Override
    public User findById(int id) {
        EntityManager em = JpaConfig.getEntityManager();

        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    /** Đồng bộ họ tên, mật khẩu, trạng thái và role ADMIN cho tài khoản cố định khi ứng dụng khởi động. */
    @Override
    public User updateFixedAdmin(
            int id, String username, String email, String fullname, String encodedPassword) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            User user = em.find(User.class, id);
            if (user == null) throw new IllegalArgumentException("Không tìm thấy tài khoản quản trị");
            user.setUsername(username);
            user.setEmail(email);
            user.setFullname(fullname);
            user.setPassword(encodedPassword);
            user.setStatus(1);
            user.setRole(Role.ADMIN);
            transaction.commit();
            return user;
        } catch (RuntimeException exception) {
            if (transaction.isActive()) transaction.rollback();
            throw exception;
        } finally {
            em.close();
        }
    }
}
