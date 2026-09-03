package vn.iotstar.models;

import java.math.BigDecimal;
import java.util.List;
import vn.iotstar.entity.Product;
import vn.iotstar.repository.IProductRepository;
import vn.iotstar.repository.ProductRepository;

public class ProductServiceImpl implements ProductService {
    private final IProductRepository repository;
    public ProductServiceImpl() { this(new ProductRepository()); }
    public ProductServiceImpl(IProductRepository repository) { this.repository = repository; }

    @Override public void insert(Product product) { validate(product); repository.insert(product); }
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
    @Override public boolean delete(int id) { return repository.delete(id); }
    @Override public Product get(int id) { return repository.findById(id); }
    @Override public List<Product> getAll() { return repository.findAll(); }
    @Override public List<Product> getPage(int page, int pageSize) {
        return repository.findPage(Math.max(page, 1), Math.max(pageSize, 1));
    }
    @Override public List<Product> getTop10Newest() { return repository.findTop10Newest(); }
    @Override public long count() { return repository.count(); }

    private void validate(Product product) {
        if (product == null) throw new IllegalArgumentException("Dữ liệu sản phẩm không hợp lệ");
        if (product.getName() == null || product.getName().isBlank())
            throw new IllegalArgumentException("Tên sản phẩm không được để trống");
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Giá sản phẩm phải lớn hơn hoặc bằng 0");
        if (product.getCategory() == null)
            throw new IllegalArgumentException("Vui lòng chọn danh mục");
        product.setName(product.getName().trim());
    }
}
