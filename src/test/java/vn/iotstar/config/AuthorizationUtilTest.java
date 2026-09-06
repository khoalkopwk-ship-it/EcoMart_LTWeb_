package vn.iotstar.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;

class AuthorizationUtilTest {
    /** Xác nhận khách và USER bị từ chối, chỉ ADMIN vượt qua quy tắc mà Filter sử dụng. */
    @Test
    void onlyAdminRoleIsAuthorized() {
        User user = new User();
        user.setRole(Role.USER);
        User admin = new User();
        admin.setRole(Role.ADMIN);

        assertFalse(AuthorizationUtil.isAdmin(null));
        assertFalse(AuthorizationUtil.isAdmin(user));
        assertTrue(AuthorizationUtil.isAdmin(admin));
    }
}
