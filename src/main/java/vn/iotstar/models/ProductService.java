package vn.iotstar.models;

import java.util.List;
import vn.iotstar.entity.Product;

public interface ProductService {
    void insert(Product product);
    void edit(Product product);
    boolean delete(int id);
    Product get(int id);
    List<Product> getAll();
    List<Product> getPage(int page, int pageSize);
    List<Product> getTop10Newest();
    long count();
}
