package vn.iotstar.utils;

import java.util.Properties;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public final class EmailUtil {
    private EmailUtil() { }

    public static void sendOtp(String receiver, String otp, String purpose) throws Exception {

        String username = System.getenv("MAIL_USERNAME");
        String password = System.getenv("MAIL_PASSWORD");

        if (username == null || username.isBlank() || password == null || password.isBlank())
            throw new IllegalStateException("Chưa cấu hình MAIL_USERNAME và MAIL_PASSWORD");

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username, "UTE EcoMart"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
        message.setSubject("Mã OTP " + purpose, "UTF-8");
        message.setText("Mã OTP của bạn là: " + otp
                + "\n\nMã có hiệu lực trong 5 phút. Không chia sẻ mã này với người khác.", "UTF-8");
        Transport.send(message);
    }
}
