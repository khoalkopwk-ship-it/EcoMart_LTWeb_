package vn.iotstar.controllers.auth;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.utils.FormValidationUtil;
import vn.iotstar.utils.OtpUtil;

@WebServlet("/forgot-password/verify")
public class VerifyForgotOtpController extends HttpServlet {
    /**
     * Include verify-forgot-otp.jsp để SiteMesh thu form OTP; doPost gọi lại khi mã sai hoặc hết hạn.
     */
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/auth/verify-forgot-otp.jsp").include(request, response);
    }
    /**
     * Đọc forgotOtp và forgotOtpTime do ForgotPasswordController lưu; dùng OtpUtil.isExpired và so sánh mã
     * người dùng nhập. Thiếu session thì quay về quên mật khẩu; sai/hết hạn thì gọi doGet kèm lỗi. Mã hợp lệ
     * đặt session.forgotVerified = true rồi chuyển /reset-password để cho phép đổi mật khẩu.
     */
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) { response.sendRedirect(request.getContextPath() + "/forgot-password"); return; }
        String expected = (String) session.getAttribute("forgotOtp");
        Long created = (Long) session.getAttribute("forgotOtpTime");
        String input = request.getParameter("otp");
        if (expected == null || created == null || OtpUtil.isExpired(created)) {
            request.setAttribute("error", "Mã OTP đã hết hạn"); doGet(request, response); return;
        }
        if (!FormValidationUtil.isValidOtp(input) || !expected.equals(input.trim())) {
            request.setAttribute("error", "Mã OTP không chính xác"); doGet(request, response); return;
        }
        session.setAttribute("forgotVerified", true);
        response.sendRedirect(request.getContextPath() + "/reset-password");
    }
}
