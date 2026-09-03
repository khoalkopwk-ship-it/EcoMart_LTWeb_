package vn.iotstar.models;

import vn.iotstar.entity.User;
import vn.iotstar.repository.IUserRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.utils.PasswordUtil;

public class UserServiceImpl implements UserService {
    private final IUserRepository repository;
    public UserServiceImpl() { this(new UserRepository()); }
    public UserServiceImpl(IUserRepository repository) { this.repository = repository; }

    @Override public void register(User user) {
        if (user == null || user.getEmail() == null || user.getEmail().isBlank()
                || user.getUsername() == null || user.getUsername().isBlank()
                || user.getFullname() == null || user.getFullname().isBlank()
                || user.getPassword() == null || user.getPassword().length() < 6) {
            throw new IllegalArgumentException("Thông tin không hợp lệ; mật khẩu tối thiểu 6 ký tự");
        }
        if (existsEmail(user.getEmail())) throw new IllegalArgumentException("Email đã được sử dụng");
        if (existsUsername(user.getUsername())) throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
        user.setEmail(user.getEmail().trim().toLowerCase());
        user.setUsername(user.getUsername().trim());
        user.setFullname(user.getFullname().trim());
        user.setPassword(PasswordUtil.hash(user.getPassword()));
        user.setStatus(1);
        repository.insert(user);
    }
    @Override public User login(String login, String password) {
        if (login == null || password == null) return null;
        User user = repository.findByLogin(login.trim());
        if (user == null || user.getStatus() != 1 || !PasswordUtil.verify(password, user.getPassword())) return null;
        return user;
    }
    @Override public boolean existsEmail(String email) {
        return email != null && repository.findByEmail(email.trim()) != null;
    }
    @Override public boolean existsUsername(String username) {
        return username != null && repository.findByUsername(username.trim()) != null;
    }
    @Override public void resetPassword(String email, String newPassword) {
        if (newPassword == null || newPassword.length() < 6)
            throw new IllegalArgumentException("Mật khẩu tối thiểu 6 ký tự");
        repository.updatePassword(email, PasswordUtil.hash(newPassword));
    }
    //Profile
    @Override
    public User updateProfile(
            int userId,
            String fullname,
            String phone,
            String images) {

        if (fullname == null || fullname.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Họ và tên không được để trống"
            );
        }

        fullname = fullname.trim();

        if (fullname.length() > 255) {
            throw new IllegalArgumentException(
                    "Họ và tên không được vượt quá 255 ký tự"
            );
        }

        phone = phone == null ? "" : phone.trim();

        if (!phone.isEmpty()
                && !phone.matches("[0-9+() .-]{8,20}")) {
            throw new IllegalArgumentException(
                    "Số điện thoại không hợp lệ"
            );
        }

        return repository.updateProfile(
                userId,
                fullname,
                phone,
                images
        );
    }
    @Override
    public User findById(int id) {
        if (id <= 0) {
            return null;
        }

        return repository.findById(id);
    }
}
