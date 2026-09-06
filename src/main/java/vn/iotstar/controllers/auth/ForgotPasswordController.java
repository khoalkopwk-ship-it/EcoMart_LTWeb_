package vn.iotstar.controllers.auth;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.models.UserService;
import vn.iotstar.models.UserServiceImpl;
import vn.iotstar.utils.EmailUtil;
import vn.iotstar.utils.FormValidationUtil;
import vn.iotstar.utils.OtpUtil;

@WebServlet("/forgot-password")
public class ForgotPasswordController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();
    /**
     * Include forgot-password.jsp để SiteMesh thu form nhập email; doPost gọi lại khi email không tồn tại hoặc gửi OTP thất
     * bại.
     */
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/auth/forgot-password.jsp").include(request, response);
    }
    /**
     * Kiểm tra email qua UserService.existsEmail, tạo mã bằng OtpUtil.generate và gửi bằng EmailUtil.sendOtp.
     * Sau khi gửi thành công lưu forgotEmail, forgotOtp, forgotOtpTime và xóa cờ forgotVerified cũ trong
     * session; chuyển /forgot-password/verify để VerifyForgotOtpController xác minh.
     */
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        email = email == null ? "" : email.trim();
        request.setAttribute("email", email);
        if (!FormValidationUtil.isValidEmail(email)) {
            request.setAttribute("error", "Email không đúng định dạng"); doGet(request, response); return;
        }
        if (!userService.existsEmail(email)) {
            request.setAttribute("error", "Email không tồn tại"); doGet(request, response); return;
        }
        String otp = OtpUtil.generate();
        try {
            EmailUtil.sendOtp(email, otp, "đặt lại mật khẩu");
        } catch (Exception exception) {
            request.setAttribute("error", "Không gửi được OTP. Hãy kiểm tra cấu hình email");
            doGet(request, response); return;
        }
        HttpSession session = request.getSession();
        session.setAttribute("forgotEmail", email);
        session.setAttribute("forgotOtp", otp);
        session.setAttribute("forgotOtpTime", System.currentTimeMillis());
        session.removeAttribute("forgotVerified");
        response.sendRedirect(request.getContextPath() + "/forgot-password/verify");
    }
}
