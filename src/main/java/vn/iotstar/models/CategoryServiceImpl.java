package vn.iotstar.models;

import java.util.List;

import vn.iotstar.entity.Category;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.ICategoryRepository;

/** Tầng Business Logic: kiểm tra dữ liệu trước khi gọi repository. */
public class CategoryServiceImpl implements CategoryService {

    private final ICategoryRepository categoryRepository;

    /**
     * Khởi tạo service với repository mặc định để controller sử dụng; chuyển việc gán phụ thuộc sang
     * constructor nhận repository.
     */
    public CategoryServiceImpl() {
        this(new CategoryRepository());
    }

    /**
     * Nhận repository từ bên ngoài và lưu làm phụ thuộc cho các hàm nghiệp vụ; cho phép thay thế nguồn truy
     * cập dữ liệu khi kiểm thử.
     */
    public CategoryServiceImpl(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Thêm danh mục sau khi kiểm tra tên bắt buộc, tối đa 255 ký tự và không trùng; chuẩn hóa tên rồi chuyển
     * CategoryRepository.insert để lưu.
     */
    @Override
    public void insert(Category category) {
        validateName(category.getName());
        category.setName(category.getName().trim());

        if (categoryRepository.findByName(category.getName()) != null) {
            throw new IllegalArgumentException("Tên danh mục đã tồn tại");
        }
        categoryRepository.insert(category);
    }

    /**
     * Kiểm tra tên bằng validateName, tìm danh mục cũ và loại trừ tên trùng với danh mục khác qua repository.
     * Chép tên/trạng thái mới, chỉ thay icon khi có ảnh mới, rồi gọi repository.update; không tìm thấy hoặc dữ
     * liệu sai thì ném IllegalArgumentException cho controller hiển thị.
     */
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

    /**
     * Chuyển id cho repository.delete; trả true nếu đã xóa, false nếu danh mục không tồn tại. Lỗi ràng buộc dữ
     * liệu được truyền về controller xử lý.
     */
    @Override
    public boolean delete(int id) {
        return categoryRepository.delete(id);
    }

    /**
     * Tra cứu danh mục qua repository theo id hoặc tên tùy overload; trả entity cho controller sử dụng, hoặc
     * null nếu không tìm thấy.
     */
    @Override
    public Category get(int id) {
        return categoryRepository.findById(id);
    }

    /**
     * Gọi repository.findAll để trả danh sách danh mục theo id, phục vụ trang quản trị, trang chủ và lựa chọn
     * danh mục của sản phẩm.
     */
    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    /**
     * Nếu keyword null/trống thì gọi getAll; ngược lại trim từ khóa rồi gọi repository.searchByName.
     * CategoryListController dùng kết quả để hiển thị danh mục.
     */
    @Override
    public List<Category> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getAll();
        }
        if (keyword.trim().length() > 255) {
            throw new IllegalArgumentException("Từ khóa tìm kiếm tối đa 255 ký tự");
        }
        return categoryRepository.searchByName(keyword.trim());
    }

    /**
     * Hàm dùng chung của insert/edit: từ chối tên null, trống hoặc dài quá 255 ký tự sau trim bằng
     * IllegalArgumentException trước khi ghi dữ liệu.
     */
    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tên danh mục không được để trống");
        }
        if (name.trim().length() > 255) {
            throw new IllegalArgumentException("Tên danh mục tối đa 255 ký tự");
        }
    }
}
