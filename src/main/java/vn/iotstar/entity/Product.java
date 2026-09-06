package vn.iotstar.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "Products")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id") private int id;
    @Column(name = "product_name", nullable = false, length = 255) private String name;
    @Column(name = "price", nullable = false, precision = 18, scale = 2) private BigDecimal price;
    @Column(name = "description") private String description;
    @Column(name = "image", length = 255) private String image;
    @Column(name = "createdDate", nullable = false) private LocalDateTime createdDate;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cate_id", nullable = false) private Category category;

    /**
     * Constructor không tham số để JPA tạo entity khi đọc dữ liệu; controller cũng có thể tạo đối tượng rỗng
     * rồi điền dữ liệu bằng setter.
     */
    public Product() { }
    /**
     * Callback @PrePersist do JPA gọi trước khi insert: gán thời gian hiện tại nếu createdDate chưa có, giữ
     * giá trị đã đặt trước. Repository kích hoạt gián tiếp khi persist entity.
     */
    @PrePersist public void prePersist() { if (createdDate == null) createdDate = LocalDateTime.now(); }
    /**
     * Trả mã định danh của Product cho tầng xử lý hoặc JSP đọc qua thuộc tính JavaBean; getter chỉ đọc dữ liệu
     * đang có trên entity.
     */
    public int getId() { return id; }
    /**
     * Gán mã định danh vào Product trong bộ nhớ để controller/service chuẩn bị dữ liệu; việc lưu xuống cơ sở
     * dữ liệu do repository và vòng đời JPA thực hiện.
     */
    public void setId(int id) { this.id = id; }
    /**
     * Trả tên của Product cho tầng xử lý hoặc JSP đọc qua thuộc tính JavaBean; getter chỉ đọc dữ liệu đang có
     * trên entity.
     */
    public String getName() { return name; }
    /**
     * Gán tên vào Product trong bộ nhớ để controller/service chuẩn bị dữ liệu; việc lưu xuống cơ sở dữ liệu do
     * repository và vòng đời JPA thực hiện.
     */
    public void setName(String name) { this.name = name; }
    /**
     * Trả giá bán của Product cho tầng xử lý hoặc JSP đọc qua thuộc tính JavaBean; getter chỉ đọc dữ liệu đang
     * có trên entity.
     */
    public BigDecimal getPrice() { return price; }
    /**
     * Gán giá bán vào Product trong bộ nhớ để controller/service chuẩn bị dữ liệu; việc lưu xuống cơ sở dữ
     * liệu do repository và vòng đời JPA thực hiện.
     */
    public void setPrice(BigDecimal price) { this.price = price; }
    /**
     * Trả mô tả của Product cho tầng xử lý hoặc JSP đọc qua thuộc tính JavaBean; getter chỉ đọc dữ liệu đang
     * có trên entity.
     */
    public String getDescription() { return description; }
    /**
     * Gán mô tả vào Product trong bộ nhớ để controller/service chuẩn bị dữ liệu; việc lưu xuống cơ sở dữ liệu
     * do repository và vòng đời JPA thực hiện.
     */
    public void setDescription(String description) { this.description = description; }
    /**
     * Trả đường dẫn ảnh sản phẩm của Product cho tầng xử lý hoặc JSP đọc qua thuộc tính JavaBean; getter chỉ
     * đọc dữ liệu đang có trên entity.
     */
    public String getImage() { return image; }
    /**
     * Gán đường dẫn ảnh sản phẩm vào Product trong bộ nhớ để controller/service chuẩn bị dữ liệu; việc lưu
     * xuống cơ sở dữ liệu do repository và vòng đời JPA thực hiện.
     */
    public void setImage(String image) { this.image = image; }
    /**
     * Trả thời điểm tạo của Product cho tầng xử lý hoặc JSP đọc qua thuộc tính JavaBean; getter chỉ đọc dữ
     * liệu đang có trên entity.
     */
    public LocalDateTime getCreatedDate() { return createdDate; }
    /**
     * Gán thời điểm tạo vào Product trong bộ nhớ để controller/service chuẩn bị dữ liệu; việc lưu xuống cơ sở
     * dữ liệu do repository và vòng đời JPA thực hiện.
     */
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    /**
     * Trả danh mục của sản phẩm của Product cho tầng xử lý hoặc JSP đọc qua thuộc tính JavaBean; getter chỉ
     * đọc dữ liệu đang có trên entity.
     */
    public Category getCategory() { return category; }
    /**
     * Gán Category cho phía sở hữu quan hệ ManyToOne; controller lấy danh mục qua CategoryService trước khi
     * gán, repository lưu khóa ngoại cate_id. Không tự thêm sản phẩm vào Category.products.
     */
    public void setCategory(Category category) { this.category = category; }
}
