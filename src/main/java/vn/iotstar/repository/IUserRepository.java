package vn.iotstar.repository;

import vn.iotstar.entity.User;

public interface IUserRepository {


    /**
     * Hợp đồng truy cập dữ liệu: Nhận User đã được service kiểm tra và băm mật khẩu; mở EntityManager qua
     * JpaConfig, persist trong transaction rồi commit. RuntimeException thì rollback nếu còn hoạt động và ném
     * lại; luôn đóng EntityManager.
     */
    void insert(User user);

    /**
     * Hợp đồng truy cập dữ liệu: Mở EntityManager qua JpaConfig và tìm User theo id; trả null nếu không tồn
     * tại, luôn đóng EntityManager. UserService.findById dùng để tải lại hồ sơ.
     */
    User findById(int id);
    /**
     * Hợp đồng truy cập dữ liệu: Gọi single với JPQL so sánh email không phân biệt hoa/thường; trả User đầu
     * tiên hoặc null để UserService.existsEmail kiểm tra tồn tại.
     */
    User findByEmail(String email);

    /**
     * Hợp đồng truy cập dữ liệu: Gọi single với JPQL so sánh username không phân biệt hoa/thường; trả User đầu
     * tiên hoặc null để UserService.existsUsername kiểm tra trùng.
     */
    User findByUsername(String username);

    /**
     * Hợp đồng truy cập dữ liệu: Tìm tối đa một User có username hoặc email khớp không phân biệt hoa/thường;
     * trả null nếu không có và luôn đóng EntityManager. UserService.login tiếp tục kiểm tra trạng thái và mật
     * khẩu.
     */
    User findByLogin(String login);

    /**
     * Hợp đồng truy cập dữ liệu: Nhận mật khẩu đã băm từ UserService.resetPassword và chạy JPQL UPDATE theo
     * email không phân biệt hoa/thường trong transaction. Commit khi thành công, rollback khi lỗi runtime và
     * luôn đóng EntityManager; không kiểm tra số dòng được cập nhật.
     */
    void updatePassword(String email, String encodedPassword);

    /**
     * Hợp đồng truy cập dữ liệu: Tìm User theo id trong transaction, báo IllegalArgumentException nếu không
     * tồn tại. Cập nhật họ tên, đổi điện thoại trống thành null và gán đường dẫn ảnh do service truyền vào;
     * JPA ghi thay đổi khi commit. Trả User để cập nhật session; rollback khi lỗi runtime và luôn đóng
     * EntityManager.
     */
    User updateProfile(
            int userId,
            String fullname,
            String phone,
            String images
    );

    /** Cập nhật các thuộc tính của tài khoản quản trị cố định trong một transaction. */
    User updateFixedAdmin(int id, String username, String email, String fullname, String encodedPassword);
}
