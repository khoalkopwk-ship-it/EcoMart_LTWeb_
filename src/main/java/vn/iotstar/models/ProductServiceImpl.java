package vn.iotstar.models;

import java.math.BigDecimal;
import java.util.List;
import vn.iotstar.entity.Product;
import vn.iotstar.repository.IProductRepository;
import vn.iotstar.repository.ProductRepository;

public class ProductServiceImpl implements ProductService {
    private static final BigDecimal MAX_PRICE = new BigDecimal("9999999999999999.99");
    private final IProductRepository repository;
    /**
     * Khởi tạo service với repository mặc định để controller sử dụng; chuyển việc gán phụ thuộc sang
     * constructor nhận repository.
     */
    public ProductServiceImpl() { this(new ProductRepository()); }
    /**
     * Nhận repository từ bên ngoài và lưu làm phụ thuộc cho các hàm nghiệp vụ; cho phép thay thế nguồn truy
     * cập dữ liệu khi kiểm thử.
     */
    public ProductServiceImpl(IProductRepository repository) { this.repository = repository; }

    /**
     * Gọi validate để kiểm tra và chuẩn hóa Product rồi chuyển repository.insert; controller nhận lỗi dữ liệu
     * nếu kiểm tra thất bại.
     */
    @Override public void insert(Product product) { validate(product); repository.insert(product); }
    /**
     * Gọi validate, tìm sản phẩm cũ qua repository.findById và báo lỗi nếu không tồn tại. Chép tên, giá, mô
     * tả, danh mục; chỉ thay ảnh khi có đường dẫn mới, giữ ngày tạo rồi gọi repository.update.
     */
    @Override public void edit(Product product) {
        validate(product);
        Product old = repository.findById(product.getId());
        if (old == null) throw new IllegalArgumentException("Không tìm thấy sản phẩm cần sửa");
        old.setName(product.getName().trim());
        old.setPrice(product.getPrice());
        old.setDescription(product.getDescription());
        old.setCategory(product.getCategory());
        if (product.getImage() != null && !product.getImage().isBlank()) old.setImage(product.getImage());
        repository.update(old);
    }
    /**
     * Ủy quyền repository.delete xóa theo id; trả true nếu đã xóa và false nếu không tồn tại cho bên gọi.
     */
    @Override public boolean delete(int id) { return repository.delete(id); }
    /**
     * Gọi repository.findById để lấy sản phẩm cho trang chi tiết/chỉnh sửa; trả null nếu không có id tương
     * ứng.
     */
    @Override public Product get(int id) { return repository.findById(id); }
    /**
     * Gọi repository.findAll để lấy sản phẩm mới nhất trước, phục vụ danh sách quản trị.
     */
    @Override public List<Product> getAll() { return repository.findAll(); }
    /**
     * Giới hạn page và pageSize tối thiểu 1 rồi gọi repository.findPage; ProductListController dùng danh sách
     * trả về để hiển thị một trang.
     */
    @Override public List<Product> getPage(int page, int pageSize) {
        return repository.findPage(Math.max(page, 1), Math.max(pageSize, 1));
    }
    /**
     * Gọi repository.findTop10Newest để lấy tối đa 10 sản phẩm mới nhất cho HomeController.
     */
    @Override public List<Product> getTop10Newest() { return repository.findTop10Newest(); }
    /**
     * Gọi repository.count để trả tổng số sản phẩm; ProductListController dùng cùng getPage để tính và hiển
     * thị phân trang.
     */
    @Override public long count() { return repository.count(); }

    /**
     * Được insert/edit gọi trước khi lưu: kiểm tra Product, tên tối đa 255 ký tự, giá không âm và phù hợp
     * DECIMAL(18,2), mô tả tối đa 255 ký tự, danh mục bắt buộc; sau đó trim tên. Ném
     * IllegalArgumentException nếu sai; việc tra cứu danh mục hợp lệ thuộc controller/service danh mục.
     */
    private void validate(Product product) {
        if (product == null) throw new IllegalArgumentException("Dữ liệu sản phẩm không hợp lệ");
        if (product.getName() == null || product.getName().isBlank())
            throw new IllegalArgumentException("Tên sản phẩm không được để trống");
        if (product.getName().trim().length() > 255)
            throw new IllegalArgumentException("Tên sản phẩm tối đa 255 ký tự");
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Giá sản phẩm phải lớn hơn hoặc bằng 0");
        if (product.getPrice().scale() > 2 || product.getPrice().compareTo(MAX_PRICE) > 0)
            throw new IllegalArgumentException("Giá tối đa 16 chữ số nguyên và 2 chữ số thập phân");
        if (product.getDescription() != null && product.getDescription().length() > 255)
            throw new IllegalArgumentException("Mô tả sản phẩm tối đa 255 ký tự");
        if (product.getCategory() == null)
            throw new IllegalArgumentException("Vui lòng chọn danh mục");
        product.setName(product.getName().trim());
    }
}
