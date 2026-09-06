package vn.iotstar.models;

import java.util.List;
import vn.iotstar.entity.Product;

public interface ProductService {
    /**
     * Hợp đồng service: Gọi validate để kiểm tra và chuẩn hóa Product rồi chuyển repository.insert; controller
     * nhận lỗi dữ liệu nếu kiểm tra thất bại.
     */
    void insert(Product product);
    /**
     * Hợp đồng service: Gọi validate, tìm sản phẩm cũ qua repository.findById và báo lỗi nếu không tồn tại.
     * Chép tên, giá, mô tả, danh mục; chỉ thay ảnh khi có đường dẫn mới, giữ ngày tạo rồi gọi
     * repository.update.
     */
    void edit(Product product);
    /**
     * Hợp đồng service: Ủy quyền repository.delete xóa theo id; trả true nếu đã xóa và false nếu không tồn tại
     * cho bên gọi.
     */
    boolean delete(int id);
    /**
     * Hợp đồng service: Gọi repository.findById để lấy sản phẩm cho trang chi tiết/chỉnh sửa; trả null nếu
     * không có id tương ứng.
     */
    Product get(int id);
    /**
     * Hợp đồng service: Gọi repository.findAll để lấy sản phẩm mới nhất trước, phục vụ danh sách quản trị.
     */
    List<Product> getAll();
    /**
     * Hợp đồng service: Giới hạn page và pageSize tối thiểu 1 rồi gọi repository.findPage;
     * ProductListController dùng danh sách trả về để hiển thị một trang.
     */
    List<Product> getPage(int page, int pageSize);
    /**
     * Hợp đồng service: Gọi repository.findTop10Newest để lấy tối đa 10 sản phẩm mới nhất cho HomeController.
     */
    List<Product> getTop10Newest();
    /**
     * Hợp đồng service: Gọi repository.count để trả tổng số sản phẩm; ProductListController dùng cùng getPage
     * để tính và hiển thị phân trang.
     */
    long count();
}
