package vn.iotstar.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FormValidationUtilTest {
    /** Kiểm tra các giá trị biên chính để frontend và backend không chấp nhận email, username, OTP sai. */
    @Test
    void shouldValidateAccountFormValues() {
        assertTrue(FormValidationUtil.isValidEmail("student@hcmute.edu.vn"));
        assertFalse(FormValidationUtil.isValidEmail("student@"));
        assertTrue(FormValidationUtil.isValidUsername("student_01"));
        assertFalse(FormValidationUtil.isValidUsername("ab"));
        assertTrue(FormValidationUtil.isValidPassword("123456"));
        assertFalse(FormValidationUtil.isValidPassword("12345"));
        assertTrue(FormValidationUtil.isValidOtp("123456"));
        assertFalse(FormValidationUtil.isValidOtp("12A456"));
    }
}
