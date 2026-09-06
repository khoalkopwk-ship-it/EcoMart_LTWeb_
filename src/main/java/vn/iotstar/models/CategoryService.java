package vn.iotstar.models;

import java.util.List;

import vn.iotstar.entity.Category;

public interface CategoryService {

    /**
     * Hợp đồng service: Thêm danh mục sau khi kiểm tra tên bắt buộc, tối đa 255 ký tự và không trùng; chuẩn
     * hóa tên rồi chuyển CategoryRepository.insert để lưu.
     */
    void insert(Category category);

    /**
     * Hợp đồng service: Kiểm tra tên bằng validateName, tìm danh mục cũ và loại trừ tên trùng với danh mục
     * khác qua repository. Chép tên/trạng thái mới, chỉ thay icon khi có ảnh mới, rồi gọi repository.update;
     * không tìm thấy hoặc dữ liệu sai thì ném IllegalArgumentException cho controller hiển thị.
     */
    void edit(Category category);

    /**
     * Hợp đồng service: Chuyển id cho repository.delete; trả true nếu đã xóa, false nếu danh mục không tồn
     * tại. Lỗi ràng buộc dữ liệu được truyền về controller xử lý.
     */
    boolean delete(int id);

    /**
     * Hợp đồng service: Tra cứu danh mục qua repository theo id hoặc tên tùy overload; trả entity cho
     * controller sử dụng, hoặc null nếu không tìm thấy.
     */
    Category get(int id);

    /**
     * Hợp đồng service: Gọi repository.findAll để trả danh sách danh mục theo id, phục vụ trang quản trị,
     * trang chủ và lựa chọn danh mục của sản phẩm.
     */
    List<Category> getAll();

    /**
     * Hợp đồng service: Nếu keyword null/trống thì gọi getAll; ngược lại trim từ khóa rồi gọi
     * repository.searchByName. CategoryListController dùng kết quả để hiển thị danh mục.
     */
    List<Category> search(String keyword);
}
