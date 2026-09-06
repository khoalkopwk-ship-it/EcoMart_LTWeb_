package vn.iotstar.repository;

import java.util.List;
import vn.iotstar.entity.Product;

public interface IProductRepository {
    /**
     * Hợp đồng truy cập dữ liệu: Được service gọi để thêm sản phẩm; chuyển persist vào inTransaction để commit
     * hoặc rollback. JPA gán id tự tăng cho entity khi lưu.
     */
    void insert(Product product);
    /**
     * Hợp đồng truy cập dữ liệu: Được service gọi để cập nhật sản phẩm; chạy EntityManager.merge trong
     * inTransaction và trả entity kết quả merge. Đối tượng trả về không còn được quản lý khi EntityManager đã
     * đóng.
     */
    Product update(Product product);
    /**
     * Hợp đồng truy cập dữ liệu: Tìm sản phẩm bằng id bên trong inTransaction; không có thì trả false, có thì
     * remove và trả true sau commit. Lỗi được rollback và truyền lên service/controller.
     */
    boolean delete(int id);
    /**
     * Hợp đồng truy cập dữ liệu: Mở EntityManager từ supplier, tìm sản phẩm theo khóa chính rồi tự đóng
     * EntityManager; trả entity hoặc null cho service và các bên gọi repository.
     */
    Product findById(int id);
    /**
     * Hợp đồng truy cập dữ liệu: Truy vấn tất cả sản phẩm, sắp createdDate giảm dần rồi id giảm dần để ổn định
     * thứ tự; đóng EntityManager và trả danh sách cho service.
     */
    List<Product> findAll();
    /**
     * Hợp đồng truy cập dữ liệu: Truy vấn sản phẩm mới nhất trước, bỏ qua (page - 1) * pageSize bản ghi và lấy
     * tối đa pageSize bản ghi. ProductService.getPage bảo đảm tham số ít nhất 1; đóng EntityManager sau khi
     * lấy kết quả.
     */
    List<Product> findPage(int page, int pageSize);
    /**
     * Hợp đồng truy cập dữ liệu: Truy vấn theo createdDate giảm dần rồi id giảm dần, giới hạn 10 kết quả và
     * đóng EntityManager; service chuyển danh sách cho trang chủ.
     */
    List<Product> findTop10Newest();
    /**
     * Hợp đồng truy cập dữ liệu: Chạy JPQL COUNT để trả tổng số sản phẩm; EntityManager được đóng sau truy
     * vấn.
     */
    long count();
}
