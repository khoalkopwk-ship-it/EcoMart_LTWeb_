package vn.iotstar.repository;

import java.util.List;

import vn.iotstar.entity.Category;

public interface ICategoryRepository {

    /**
     * Hợp đồng truy cập dữ liệu: Được service gọi để thêm danh mục; chuyển persist vào executeInTransaction để
     * commit hoặc rollback. JPA gán id tự tăng cho entity khi lưu.
     */
    void insert(Category category);

    /**
     * Hợp đồng truy cập dữ liệu: Được service gọi để cập nhật danh mục; chạy EntityManager.merge trong
     * executeInTransaction và trả entity kết quả merge. Đối tượng trả về không còn được quản lý khi
     * EntityManager đã đóng.
     */
    Category update(Category category);

    /**
     * Hợp đồng truy cập dữ liệu: Tìm danh mục bằng id bên trong executeInTransaction; không có thì trả false,
     * có thì remove và trả true sau commit. Lỗi được rollback và truyền lên service/controller.
     */
    boolean delete(int id);

    /**
     * Hợp đồng truy cập dữ liệu: Mở EntityManager từ supplier, tìm danh mục theo khóa chính rồi tự đóng
     * EntityManager; trả entity hoặc null cho service và các bên gọi repository.
     */
    Category findById(int id);

    /**
     * Hợp đồng truy cập dữ liệu: Tìm danh mục theo tên không phân biệt hoa/thường bằng JPQL có tham số; lấy
     * tối đa một kết quả hoặc null, tự đóng EntityManager. CategoryService dùng để tra cứu và ngăn tên trùng.
     */
    Category findByName(String name);

    /**
     * Hợp đồng truy cập dữ liệu: Chạy named query Category.findAll khai báo trên entity để lấy danh mục theo
     * id tăng dần; đóng EntityManager rồi trả danh sách cho service.
     */
    List<Category> findAll();

    /**
     * Hợp đồng truy cập dữ liệu: Tìm danh mục có tên chứa keyword bằng LIKE không phân biệt hoa/thường, sắp
     * theo id và đóng EntityManager. CategoryService.search đã xử lý từ khóa trống trước khi gọi.
     */
    List<Category> searchByName(String keyword);

    /**
     * Hợp đồng truy cập dữ liệu: Chạy JPQL COUNT để trả tổng số danh mục; EntityManager được đóng sau truy
     * vấn.
     */
    long count();
}
