package vn.iotstar.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import vn.iotstar.config.FixedAdminConfig;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.IUserRepository;

class UserServiceRoleTest {
    /** Xác nhận service luôn ghi USER dù entity đầu vào cố gán ADMIN. */
    @Test
    void registerAlwaysCreatesUserRole() {
        FakeUserRepository repository = new FakeUserRepository();
        UserService service = new UserServiceImpl(repository);
        User user = user("member", "member@example.com", "Member", "Secret1");
        user.setRole(Role.ADMIN);

        service.register(user);

        assertEquals(Role.USER, repository.users.get(0).getRole());
        assertNotEquals("Secret1", repository.users.get(0).getPassword());
    }

    /** Xác nhận form/service đăng ký không thể chiếm username cố định của ADMIN. */
    @Test
    void registerRejectsReservedAdminIdentity() {
        UserService service = new UserServiceImpl(new FakeUserRepository());
        User user = user(FixedAdminConfig.username(), "other@example.com", "Other", "Secret1");
        assertThrows(IllegalArgumentException.class, () -> service.register(user));
    }

    /** Xác nhận bootstrap tạo ADMIN và tài khoản này đăng nhập được bằng mật khẩu cấu hình. */
    @Test
    void fixedAdminIsCreatedAndCanLogin() {
        FakeUserRepository repository = new FakeUserRepository();
        UserService service = new UserServiceImpl(repository);

        service.ensureFixedAdmin();
        User admin = service.login(FixedAdminConfig.username(), FixedAdminConfig.password());

        assertNotNull(admin);
        assertEquals(Role.ADMIN, admin.getRole());
    }

    /** Tạo dữ liệu đăng ký hợp lệ cho từng kịch bản test. */
    private User user(String username, String email, String fullname, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setFullname(fullname);
        user.setPassword(password);
        return user;
    }

    /** Repository trong bộ nhớ giúp kiểm tra quy tắc service mà không phụ thuộc SQL Server. */
    private static class FakeUserRepository implements IUserRepository {
        private final List<User> users = new ArrayList<>();

        @Override public void insert(User user) { user.setId(users.size() + 1); users.add(user); }
        @Override public User findById(int id) {
            return users.stream().filter(user -> user.getId() == id).findFirst().orElse(null);
        }
        @Override public User findByEmail(String email) {
            return users.stream().filter(user -> user.getEmail().equalsIgnoreCase(email)).findFirst().orElse(null);
        }
        @Override public User findByUsername(String username) {
            return users.stream().filter(user -> user.getUsername().equalsIgnoreCase(username)).findFirst().orElse(null);
        }
        @Override public User findByLogin(String login) {
            return users.stream().filter(user -> user.getUsername().equalsIgnoreCase(login)
                    || user.getEmail().equalsIgnoreCase(login)).findFirst().orElse(null);
        }
        @Override public void updatePassword(String email, String encodedPassword) {
            User user = findByEmail(email);
            if (user != null) user.setPassword(encodedPassword);
        }
        @Override public User updateProfile(int id, String fullname, String phone, String images) {
            User user = findById(id);
            user.setFullname(fullname); user.setPhone(phone); user.setImages(images);
            return user;
        }
        @Override public User updateFixedAdmin(
                int id, String username, String email, String fullname, String encodedPassword) {
            User user = findById(id);
            user.setUsername(username); user.setEmail(email);
            user.setFullname(fullname); user.setPassword(encodedPassword);
            user.setStatus(1); user.setRole(Role.ADMIN);
            return user;
        }
    }
}
