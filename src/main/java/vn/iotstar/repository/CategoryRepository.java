package vn.iotstar.repository;

import java.util.List;
import java.util.function.Supplier;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.iotstar.entity.Category;
import vn.iotstar.utils.JpaConfig;

/** Tầng Data Access: mọi thao tác SQL được thay bằng EntityManager và JPQL. */
public class CategoryRepository implements ICategoryRepository {

    private final Supplier<EntityManager> entityManagerSupplier;

    public CategoryRepository() {
        this(JpaConfig::getEntityManager);
    }

    /** Constructor này giúp repository có thể kiểm thử bằng CSDL H2 riêng. */
    public CategoryRepository(Supplier<EntityManager> entityManagerSupplier) {
        this.entityManagerSupplier = entityManagerSupplier;
    }

    @Override
    public void insert(Category category) {
        executeInTransaction(entityManager -> {
            entityManager.persist(category);
            return null;
        });
    }

    @Override
    public Category update(Category category) {
        return executeInTransaction(entityManager -> entityManager.merge(category));
    }

    @Override
    public boolean delete(int id) {
        return executeInTransaction(entityManager -> {
            Category category = entityManager.find(Category.class, id);
            if (category == null) {
                return false;
            }
            entityManager.remove(category);
            return true;
        });
    }

    @Override
    public Category findById(int id) {
        try (EntityManager entityManager = entityManagerSupplier.get()) {
            return entityManager.find(Category.class, id);
        }
    }

    @Override
    public Category findByName(String name) {
        try (EntityManager entityManager = entityManagerSupplier.get()) {
            return entityManager.createQuery(
                            "SELECT c FROM Category c WHERE LOWER(c.name) = LOWER(:name)",
                            Category.class)
                    .setParameter("name", name)
                    .setMaxResults(1)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        }
    }

    @Override
    public List<Category> findAll() {
        try (EntityManager entityManager = entityManagerSupplier.get()) {
            return entityManager.createNamedQuery("Category.findAll", Category.class)
                    .getResultList();
        }
    }

    @Override
    public List<Category> searchByName(String keyword) {
        try (EntityManager entityManager = entityManagerSupplier.get()) {
            return entityManager.createQuery(
                            "SELECT c FROM Category c "
                                    + "WHERE LOWER(c.name) LIKE LOWER(:keyword) ORDER BY c.id",
                            Category.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        }
    }

    @Override
    public long count() {
        try (EntityManager entityManager = entityManagerSupplier.get()) {
            return entityManager.createQuery(
                            "SELECT COUNT(c) FROM Category c", Long.class)
                    .getSingleResult();
        }
    }

    private <T> T executeInTransaction(TransactionWork<T> work) {
        EntityManager entityManager = entityManagerSupplier.get();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            T result = work.execute(entityManager);
            transaction.commit();
            return result;
        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        } finally {
            entityManager.close();
        }
    }

    @FunctionalInterface
    private interface TransactionWork<T> {
        T execute(EntityManager entityManager);
    }
}
