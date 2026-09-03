package vn.iotstar.utils;

import java.security.SecureRandom;

public final class OtpUtil {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final long VALID_TIME_MILLIS = 5 * 60 * 1000L;
    private OtpUtil() { }
    public static String generate() { return String.valueOf(100000 + RANDOM.nextInt(900000)); }
    public static boolean isExpired(long createdTime) {
        return System.currentTimeMillis() - createdTime > VALID_TIME_MILLIS;
    }
}
