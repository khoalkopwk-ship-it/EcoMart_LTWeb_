package vn.iotstar.repository;

import java.util.List;
import java.util.function.Supplier;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.iotstar.entity.Product;
import vn.iotstar.utils.JpaConfig;

public class ProductRepository implements IProductRepository {
    private final Supplier<EntityManager> entityManagerSupplier;

    public ProductRepository() { this(JpaConfig::getEntityManager); }
    public ProductRepository(Supplier<EntityManager> supplier) { this.entityManagerSupplier = supplier; }

    @Override public void insert(Product product) { inTransaction(em -> { em.persist(product); return null; }); }
    @Override public Product update(Product product) { return inTransaction(em -> em.merge(product)); }
    @Override public boolean delete(int id) {
        return inTransaction(em -> {
            Product product = em.find(Product.class, id);
            if (product == null) return false;
            em.remove(product);
            return true;
        });
    }
    @Override public Product findById(int id) {
        try (EntityManager em = entityManagerSupplier.get()) { return em.find(Product.class, id); }
    }
    @Override public List<Product> findAll() {
        try (EntityManager em = entityManagerSupplier.get()) {
            return em.createQuery("SELECT p FROM Product p ORDER BY p.createdDate DESC, p.id DESC", Product.class)
                    .getResultList();
        }
    }
    @Override public List<Product> findPage(int page, int pageSize) {
        try (EntityManager em = entityManagerSupplier.get()) {
            return em.createQuery("SELECT p FROM Product p ORDER BY p.createdDate DESC, p.id DESC", Product.class)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        }
    }
    @Override public List<Product> findTop10Newest() {
        try (EntityManager em = entityManagerSupplier.get()) {
            return em.createQuery("SELECT p FROM Product p ORDER BY p.createdDate DESC, p.id DESC", Product.class)
                    .setMaxResults(10)
                    .getResultList();
        }
    }
    @Override public long count() {
        try (EntityManager em = entityManagerSupplier.get()) {
            return em.createQuery("SELECT COUNT(p) FROM Product p", Long.class).getSingleResult();
        }
    }

    private <T> T inTransaction(Work<T> work) {
        EntityManager em = entityManagerSupplier.get();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            T result = work.execute(em);
            transaction.commit();
            return result;
        } catch (RuntimeException exception) {
            if (transaction.isActive()) transaction.rollback();
            throw exception;
        } finally {
            em.close();
        }
    }

    @FunctionalInterface private interface Work<T> { T execute(EntityManager entityManager); }
}
