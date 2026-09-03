package vn.iotstar.models;

import java.util.List;

import vn.iotstar.entity.Category;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.ICategoryRepository;

/** Tầng Business Logic: kiểm tra dữ liệu trước khi gọi repository. */
public class CategoryServiceImpl implements CategoryService {

    private final ICategoryRepository categoryRepository;

    public CategoryServiceImpl() {
        this(new CategoryRepository());
    }

    public CategoryServiceImpl(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void insert(Category category) {
        validateName(category.getName());
        category.setName(category.getName().trim());

        if (categoryRepository.findByName(category.getName()) != null) {
            throw new IllegalArgumentException("Tên danh mục đã tồn tại");
        }
        categoryRepository.insert(category);
    }

    @Override
    public void edit(Category newCategory) {
        validateName(newCategory.getName());

        Category oldCategory = categoryRepository.findById(newCategory.getId());
        if (oldCategory == null) {
            throw new IllegalArgumentException("Không tìm thấy danh mục cần sửa");
        }

        Category sameName = categoryRepository.findByName(newCategory.getName().trim());
        if (sameName != null && sameName.getId() != newCategory.getId()) {
            throw new IllegalArgumentException("Tên danh mục đã tồn tại");
        }

        oldCategory.setName(newCategory.getName().trim());
        oldCategory.setStatus(newCategory.getStatus());
        if (newCategory.getIcon() != null && !newCategory.getIcon().isBlank()) {
            oldCategory.setIcon(newCategory.getIcon());
        }
        categoryRepository.update(oldCategory);
    }

    @Override
    public boolean delete(int id) {
        return categoryRepository.delete(id);
    }

    @Override
    public Category get(int id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category get(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getAll();
        }
        return categoryRepository.searchByName(keyword.trim());
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tên danh mục không được để trống");
        }
        if (name.trim().length() > 255) {
            throw new IllegalArgumentException("Tên danh mục tối đa 255 ký tự");
        }
    }
}
