package vn.iotstar.repository;

import java.util.List;
import java.util.function.Supplier;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.iotstar.entity.Product;
import vn.iotstar.utils.JpaConfig;

public class ProductRepository implements IProductRepository {
    private final Supplier<EntityManager> entityManagerSupplier;

    /**
     * Khởi tạo repository với JpaConfig.getEntityManager làm nguồn EntityManager cho mỗi thao tác.
     */
    public ProductRepository() { this(JpaConfig::getEntityManager); }
    /**
     * Nhận supplier tạo EntityManager, dùng chung cho các hàm đọc/ghi; kiểm thử có thể truyền factory H2 thay
     * cho cấu hình cơ sở dữ liệu chính.
     */
    public ProductRepository(Supplier<EntityManager> supplier) { this.entityManagerSupplier = supplier; }

    /**
     * Được service gọi để thêm sản phẩm; chuyển persist vào inTransaction để commit hoặc rollback. JPA gán id
     * tự tăng cho entity khi lưu.
     */
    @Override public void insert(Product product) { inTransaction(em -> { em.persist(product); return null; }); }
    /**
     * Được service gọi để cập nhật sản phẩm; chạy EntityManager.merge trong inTransaction và trả entity kết
     * quả merge. Đối tượng trả về không còn được quản lý khi EntityManager đã đóng.
     */
    @Override public Product update(Product product) { return inTransaction(em -> em.merge(product)); }
    /**
     * Tìm sản phẩm bằng id bên trong inTransaction; không có thì trả false, có thì remove và trả true sau
     * commit. Lỗi được rollback và truyền lên service/controller.
     */
    @Override public boolean delete(int id) {
        return inTransaction(em -> {
            Product product = em.find(Product.class, id);
            if (product == null) return false;
            em.remove(product);
            return true;
        });
    }
    /**
     * Mở EntityManager từ supplier, tìm sản phẩm theo khóa chính rồi tự đóng EntityManager; trả entity hoặc
     * null cho service và các bên gọi repository.
     */
    @Override public Product findById(int id) {
        try (EntityManager em = entityManagerSupplier.get()) { return em.find(Product.class, id); }
    }
    /**
     * Truy vấn tất cả sản phẩm, sắp createdDate giảm dần rồi id giảm dần để ổn định thứ tự; đóng EntityManager
     * và trả danh sách cho service.
     */
    @Override public List<Product> findAll() {
        try (EntityManager em = entityManagerSupplier.get()) {
            return em.createQuery("SELECT p FROM Product p ORDER BY p.createdDate DESC, p.id DESC", Product.class)
                    .getResultList();
        }
    }
    /**
     * Truy vấn sản phẩm mới nhất trước, bỏ qua (page - 1) * pageSize bản ghi và lấy tối đa pageSize bản ghi.
     * ProductService.getPage bảo đảm tham số ít nhất 1; đóng EntityManager sau khi lấy kết quả.
     */
    @Override public List<Product> findPage(int page, int pageSize) {
        try (EntityManager em = entityManagerSupplier.get()) {
            return em.createQuery("SELECT p FROM Product p ORDER BY p.createdDate DESC, p.id DESC", Product.class)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        }
    }
    /**
     * Truy vấn theo createdDate giảm dần rồi id giảm dần, giới hạn 10 kết quả và đóng EntityManager; service
     * chuyển danh sách cho trang chủ.
     */
    @Override public List<Product> findTop10Newest() {
        try (EntityManager em = entityManagerSupplier.get()) {
            return em.createQuery("SELECT p FROM Product p ORDER BY p.createdDate DESC, p.id DESC", Product.class)
                    .setMaxResults(10)
                    .getResultList();
        }
    }
    /**
     * Chạy JPQL COUNT để trả tổng số sản phẩm; EntityManager được đóng sau truy vấn.
     */
    @Override public long count() {
        try (EntityManager em = entityManagerSupplier.get()) {
            return em.createQuery("SELECT COUNT(p) FROM Product p", Long.class).getSingleResult();
        }
    }

    /**
     * Hàm dùng chung của insert/update/delete: mở EntityManager từ supplier, bắt đầu transaction rồi gọi
     * work.execute. Thành công commit và trả kết quả; RuntimeException thì rollback nếu còn hoạt động rồi ném
     * lại; luôn đóng EntityManager trong finally.
     */
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

    @FunctionalInterface private interface Work<T> {
        /**
         * Hợp đồng callback nhận EntityManager đang nằm trong transaction; inTransaction gọi phần việc do
         * insert/update/delete truyền vào và quản lý commit, rollback, đóng tài nguyên bên ngoài callback.
         */
        T execute(EntityManager entityManager);
    }
}
