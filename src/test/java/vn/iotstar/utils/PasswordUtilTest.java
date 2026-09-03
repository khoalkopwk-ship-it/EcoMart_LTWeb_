package vn.iotstar.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class PasswordUtilTest {
    @Test void shouldHashWithSaltAndVerifyPassword() {
        String first = PasswordUtil.hash("MatKhau123");
        String second = PasswordUtil.hash("MatKhau123");
        assertNotEquals(first, second);
        assertTrue(PasswordUtil.verify("MatKhau123", first));
        assertFalse(PasswordUtil.verify("sai-mat-khau", first));
    }
}
