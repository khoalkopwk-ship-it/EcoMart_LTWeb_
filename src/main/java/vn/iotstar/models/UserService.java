package vn.iotstar.models;

import vn.iotstar.entity.User;

public interface UserService {
    void register(User user);
    User login(String login, String password);
    boolean existsEmail(String email);
    boolean existsUsername(String username);
    void resetPassword(String email, String newPassword);
    User updateProfile(
            int userId,
            String fullname,
            String phone,
            String images
    );
    User findById(int id);
}
