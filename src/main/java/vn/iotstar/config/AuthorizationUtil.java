package vn.iotstar.config;

import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;

/** Quy tắc đọc role từ account session, dùng chung để kiểm tra quyền backend. */
public final class AuthorizationUtil {
    private AuthorizationUtil() { }

    /** Chỉ trả true khi đối tượng session là User mang role ADMIN. */
    public static boolean isAdmin(Object account) {
        return account instanceof User user && user.getRole() == Role.ADMIN;
    }
}
