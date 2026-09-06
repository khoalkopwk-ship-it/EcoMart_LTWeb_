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

    /**
     * Khởi tạo repository với JpaConfig.getEntityManager làm nguồn EntityManager cho mỗi thao tác.
     */
    public CategoryRepository() {
        this(JpaConfig::getEntityManager);
    }

    /**
     * Nhận supplier tạo EntityManager, dùng chung cho các hàm đọc/ghi; kiểm thử có thể truyền factory H2 thay
     * cho cấu hình cơ sở dữ liệu chính.
     */
    public CategoryRepository(Supplier<EntityManager> entityManagerSupplier) {
        this.entityManagerSupplier = entityManagerSupplier;
    }

    /**
     * Được service gọi để thêm danh mục; chuyển persist vào executeInTransaction để commit hoặc rollback. JPA
     * gán id tự tăng cho entity khi lưu.
     */
    @Override
    public void insert(Category category) {
        executeInTransaction(entityManager -> {
            entityManager.persist(category);
            return null;
        });
    }

    /**
     * Được service gọi để cập nhật danh mục; chạy EntityManager.merge trong executeInTransaction và trả entity
     * kết quả merge. Đối tượng trả về không còn được quản lý khi EntityManager đã đóng.
     */
    @Override
    public Category update(Category category) {
        return executeInTransaction(entityManager -> entityManager.merge(category));
    }

    /**
     * Tìm danh mục bằng id bên trong executeInTransaction; không có thì trả false, có thì remove và trả true
     * sau commit. Lỗi được rollback và truyền lên service/controller.
     */
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

    /**
     * Mở EntityManager từ supplier, tìm danh mục theo khóa chính rồi tự đóng EntityManager; trả entity hoặc
     * null cho service và các bên gọi repository.
     */
    @Override
    public Category findById(int id) {
        try (EntityManager entityManager = entityManagerSupplier.get()) {
            return entityManager.find(Category.class, id);
        }
    }

    /**
     * Tìm danh mục theo tên không phân biệt hoa/thường bằng JPQL có tham số; lấy tối đa một kết quả hoặc null,
     * tự đóng EntityManager. CategoryService dùng để tra cứu và ngăn tên trùng.
     */
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

    /**
     * Chạy named query Category.findAll khai báo trên entity để lấy danh mục theo id tăng dần; đóng
     * EntityManager rồi trả danh sách cho service.
     */
    @Override
    public List<Category> findAll() {
        try (EntityManager entityManager = entityManagerSupplier.get()) {
            return entityManager.createNamedQuery("Category.findAll", Category.class)
                    .getResultList();
        }
    }

    /**
     * Tìm danh mục có tên chứa keyword bằng LIKE không phân biệt hoa/thường, sắp theo id và đóng
     * EntityManager. CategoryService.search đã xử lý từ khóa trống trước khi gọi.
     */
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

    /**
     * Chạy JPQL COUNT để trả tổng số danh mục; EntityManager được đóng sau truy vấn.
     */
    @Override
    public long count() {
        try (EntityManager entityManager = entityManagerSupplier.get()) {
            return entityManager.createQuery(
                            "SELECT COUNT(c) FROM Category c", Long.class)
                    .getSingleResult();
        }
    }

    /**
     * Hàm dùng chung của insert/update/delete: mở EntityManager từ supplier, bắt đầu transaction rồi gọi
     * work.execute. Thành công commit và trả kết quả; RuntimeException thì rollback nếu còn hoạt động rồi ném
     * lại; luôn đóng EntityManager trong finally.
     */
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
        /**
         * Hợp đồng callback nhận EntityManager đang nằm trong transaction; executeInTransaction gọi phần việc
         * do insert/update/delete truyền vào và quản lý commit, rollback, đóng tài nguyên bên ngoài callback.
         */
        T execute(EntityManager entityManager);
    }
}
