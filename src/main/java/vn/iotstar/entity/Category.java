package vn.iotstar.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Category")
@NamedQuery(name = "Category.findAll", query = "SELECT c FROM Category c ORDER BY c.id")
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_id")
    private int id;

    @Column(name = "cate_name", nullable = false, unique = true, length = 255)
    private String name;

    @Column(name = "icons", length = 255)
    private String icon;

    @Column(name = "status", nullable = false)
    private int status = 1;

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    /**
     * Constructor không tham số để JPA tạo entity khi đọc dữ liệu; controller cũng có thể tạo đối tượng rỗng
     * rồi điền dữ liệu bằng setter.
     */
    public Category() { }
    /**
     * Tạo Category từ các trường được truyền vào cho luồng thêm/sửa danh mục; overload thiếu status chuyển
     * sang constructor đầy đủ với trạng thái mặc định 1. Constructor chỉ gán dữ liệu, CategoryService mới kiểm
     * tra và repository mới ghi vào cơ sở dữ liệu.
     */
    public Category(int id, String name, String icon) { this(id, name, icon, 1); }
    /**
     * Tạo Category từ các trường được truyền vào cho luồng thêm/sửa danh mục; overload thiếu status chuyển
     * sang constructor đầy đủ với trạng thái mặc định 1. Constructor chỉ gán dữ liệu, CategoryService mới kiểm
     * tra và repository mới ghi vào cơ sở dữ liệu.
     */
    public Category(int id, String name, String icon, int status) {
        this.id = id; this.name = name; this.icon = icon; this.status = status;
    }
    /**
     * Tạo Category từ các trường được truyền vào cho luồng thêm/sửa danh mục; overload thiếu status chuyển
     * sang constructor đầy đủ với trạng thái mặc định 1. Constructor chỉ gán dữ liệu, CategoryService mới kiểm
     * tra và repository mới ghi vào cơ sở dữ liệu.
     */
    public Category(String name, String icon) { this(name, icon, 1); }
    /**
     * Tạo Category từ các trường được truyền vào cho luồng thêm/sửa danh mục; overload thiếu status chuyển
     * sang constructor đầy đủ với trạng thái mặc định 1. Constructor chỉ gán dữ liệu, CategoryService mới kiểm
     * tra và repository mới ghi vào cơ sở dữ liệu.
     */
    public Category(String name, String icon, int status) {
        this.name = name; this.icon = icon; this.status = status;
    }

    /**
     * Trả mã định danh của Category cho tầng xử lý hoặc JSP đọc qua thuộc tính JavaBean; getter chỉ đọc dữ
     * liệu đang có trên entity.
     */
    public int getId() { return id; }
    /**
     * Gán mã định danh vào Category trong bộ nhớ để controller/service chuẩn bị dữ liệu; việc lưu xuống cơ sở
     * dữ liệu do repository và vòng đời JPA thực hiện.
     */
    public void setId(int id) { this.id = id; }
    /**
     * Trả tên của Category cho tầng xử lý hoặc JSP đọc qua thuộc tính JavaBean; getter chỉ đọc dữ liệu đang có
     * trên entity.
     */
    public String getName() { return name; }
    /**
     * Gán tên vào Category trong bộ nhớ để controller/service chuẩn bị dữ liệu; việc lưu xuống cơ sở dữ liệu
     * do repository và vòng đời JPA thực hiện.
     */
    public void setName(String name) { this.name = name; }
    /**
     * Trả đường dẫn ảnh danh mục của Category cho tầng xử lý hoặc JSP đọc qua thuộc tính JavaBean; getter chỉ
     * đọc dữ liệu đang có trên entity.
     */
    public String getIcon() { return icon; }
    /**
     * Gán đường dẫn ảnh danh mục vào Category trong bộ nhớ để controller/service chuẩn bị dữ liệu; việc lưu
     * xuống cơ sở dữ liệu do repository và vòng đời JPA thực hiện.
     */
    public void setIcon(String icon) { this.icon = icon; }
    /**
     * Trả trạng thái của Category cho tầng xử lý hoặc JSP đọc qua thuộc tính JavaBean; getter chỉ đọc dữ liệu
     * đang có trên entity.
     */
    public int getStatus() { return status; }
    /**
     * Gán trạng thái vào Category trong bộ nhớ để controller/service chuẩn bị dữ liệu; việc lưu xuống cơ sở dữ
     * liệu do repository và vòng đời JPA thực hiện.
     */
    public void setStatus(int status) { this.status = status; }
    /**
     * Trả danh sách Product của quan hệ mappedBy = category; quan hệ này mặc định tải LAZY nên truy cập dữ
     * liệu chưa tải cần EntityManager còn mở.
     */
    public List<Product> getProducts() { return products; }
    /**
     * Gán danh sách sản phẩm trong bộ nhớ; không tự đồng bộ Product.category. Phía Product.category sở hữu
     * khóa ngoại và phải được gán riêng khi liên kết sản phẩm với danh mục.
     */
    public void setProducts(List<Product> products) { this.products = products; }
}
