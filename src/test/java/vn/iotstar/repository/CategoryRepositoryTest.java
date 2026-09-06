package vn.iotstar.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vn.iotstar.entity.Category;

class CategoryRepositoryTest {

    private static EntityManagerFactory factory;
    private static ICategoryRepository repository;

    /**
     * JUnit gọi một lần trước các test: tạo factory từ persistence unit jpa-test và truyền supplier cho
     * CategoryRepository để thao tác trên cơ sở dữ liệu H2 kiểm thử.
     */
    @BeforeAll
    static void setUp() {
        factory = Persistence.createEntityManagerFactory("jpa-test");
        repository = new CategoryRepository(factory::createEntityManager);
    }

    /**
     * JUnit gọi sau các test để đóng factory nếu còn mở và giải phóng cơ sở dữ liệu/tài nguyên kiểm thử.
     */
    @AfterAll
    static void tearDown() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }

    /**
     * Kiểm thử chuỗi repository thêm → tìm theo id/tên không phân biệt hoa thường → sửa → tìm theo từ khóa →
     * xóa; kiểm tra id tự tăng, dữ liệu đọc lại và count ở từng mốc phù hợp.
     */
    @Test
    void shouldCompleteCategoryCrudFlow() {
        Category category = new Category("Quần áo nam", "category/nam.png");

        repository.insert(category);
        assertTrue(category.getId() > 0);
        assertEquals(1, repository.count());

        Category found = repository.findById(category.getId());
        assertNotNull(found);
        assertEquals("Quần áo nam", found.getName());
        assertNotNull(repository.findByName("QUẦN ÁO NAM"));

        found.setName("Thời trang nam");
        repository.update(found);
        assertEquals("Thời trang nam", repository.findById(found.getId()).getName());
        assertFalse(repository.searchByName("trang").isEmpty());

        assertTrue(repository.delete(found.getId()));
        assertNull(repository.findById(found.getId()));
        assertEquals(0, repository.count());
    }
}
