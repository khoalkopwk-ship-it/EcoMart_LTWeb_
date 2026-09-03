package vn.iotstar.repository;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.iotstar.entity.User;
import vn.iotstar.utils.JpaConfig;

public class UserRepository implements IUserRepository {
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

    @Override public User findByEmail(String email) {
        return single("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:value)", email);
    }
    @Override public User findByUsername(String username) {
        return single("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:value)", username);
    }
    @Override public User findByLogin(String login) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            List<User> users = em.createQuery(
                    "SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:login) OR LOWER(u.email) = LOWER(:login)",
                    User.class).setParameter("login", login).setMaxResults(1).getResultList();
            return users.isEmpty() ? null : users.get(0);
        } finally { em.close(); }
    }
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

    private User single(String jpql, String value) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            List<User> users = em.createQuery(jpql, User.class)
                    .setParameter("value", value).setMaxResults(1).getResultList();
            return users.isEmpty() ? null : users.get(0);
        } finally { em.close(); }
    }
    // profile
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
    @Override
    public User findById(int id) {
        EntityManager em = JpaConfig.getEntityManager();

        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }
}
