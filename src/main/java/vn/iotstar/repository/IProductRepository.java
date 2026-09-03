package vn.iotstar.repository;

import java.util.List;
import vn.iotstar.entity.Product;

public interface IProductRepository {
    void insert(Product product);
    Product update(Product product);
    boolean delete(int id);
    Product findById(int id);
    List<Product> findAll();
    List<Product> findPage(int page, int pageSize);
    List<Product> findTop10Newest();
    long count();
}
