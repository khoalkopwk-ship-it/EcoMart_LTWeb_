package vn.iotstar.controllers.auth;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.models.UserService;
import vn.iotstar.models.UserServiceImpl;
import vn.iotstar.utils.FormValidationUtil;

@WebServlet("/reset-password")
public class ResetPasswordController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();
    /**
     * Chỉ hiển thị reset-password.jsp khi session có forgotVerified = true do VerifyForgotOtpController đặt;
     * chưa xác minh thì chuyển /forgot-password.
     */
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !Boolean.TRUE.equals(session.getAttribute("forgotVerified"))) {
            response.sendRedirect(request.getContextPath() + "/forgot-password"); return;
        }
        request.getRequestDispatcher("/views/auth/reset-password.jsp").include(request, response);
    }
    /**
     * Yêu cầu forgotEmail và cờ forgotVerified trong session, đối chiếu mật khẩu xác nhận rồi gọi
     * UserService.resetPassword để kiểm tra độ dài, băm và cập nhật qua repository. Lỗi trả lại form qua
     * doGet; thành công xóa dữ liệu OTP quên mật khẩu, đặt thông báo và chuyển /login.
     */
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String email = session == null ? null : (String) session.getAttribute("forgotEmail");
        if (email == null || !Boolean.TRUE.equals(session.getAttribute("forgotVerified"))) {
            response.sendRedirect(request.getContextPath() + "/forgot-password"); return;
        }
        String password = request.getParameter("password");
        if (!FormValidationUtil.isValidPassword(password)) {
            request.setAttribute("error", "Mật khẩu phải từ 6 đến 255 ký tự"); doGet(request, response); return;
        }
        if (password == null || !password.equals(request.getParameter("confirmPassword"))) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp"); doGet(request, response); return;
        }
        try { userService.resetPassword(email, password); }
        catch (IllegalArgumentException exception) {
            request.setAttribute("error", exception.getMessage()); doGet(request, response); return;
        }
        session.removeAttribute("forgotEmail"); session.removeAttribute("forgotOtp");
        session.removeAttribute("forgotOtpTime"); session.removeAttribute("forgotVerified");
        session.setAttribute("success", "Đổi mật khẩu thành công");
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
