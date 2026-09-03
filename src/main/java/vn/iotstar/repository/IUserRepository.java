package vn.iotstar.repository;

import vn.iotstar.entity.User;

public interface IUserRepository {


    void insert(User user);

    User findById(int id);
    User findByEmail(String email);

    User findByUsername(String username);

    User findByLogin(String login);

    void updatePassword(String email, String encodedPassword);

    User updateProfile(
            int userId,
            String fullname,
            String phone,
            String images
    );
}