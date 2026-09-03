package vn.iotstar.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.math.BigDecimal;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;

class ProductRepositoryTest {
    private static EntityManagerFactory factory;
    private static ICategoryRepository categories;
    private static IProductRepository products;

    @BeforeAll static void setUp() {
        factory = Persistence.createEntityManagerFactory("jpa-test");
        categories = new CategoryRepository(factory::createEntityManager);
        products = new ProductRepository(factory::createEntityManager);
    }
    @AfterAll static void tearDown() { if (factory != null && factory.isOpen()) factory.close(); }

    @Test void shouldCreatePaginateAndGetTenNewestProducts() {
        Category category = new Category("Test Product Category", null);
        categories.insert(category);
        for (int index = 1; index <= 12; index++) {
            Product product = new Product();
            product.setName("Sản phẩm " + index);
            product.setPrice(BigDecimal.valueOf(index * 1000L));
            product.setDescription("Mô tả " + index);
            product.setCategory(category);
            products.insert(product);
            assertNotNull(products.findById(product.getId()));
        }
        assertEquals(12, products.count());
        assertEquals(6, products.findPage(1, 6).size());
        assertEquals(10, products.findTop10Newest().size());
    }
}
