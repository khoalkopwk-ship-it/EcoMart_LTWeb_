package vn.iotstar.controllers.auth;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.models.UserService;
import vn.iotstar.models.UserServiceImpl;

@WebServlet("/reset-password")
public class ResetPasswordController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !Boolean.TRUE.equals(session.getAttribute("forgotVerified"))) {
            response.sendRedirect(request.getContextPath() + "/forgot-password"); return;
        }
        request.getRequestDispatcher("/views/auth/reset-password.jsp").forward(request, response);
    }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String email = session == null ? null : (String) session.getAttribute("forgotEmail");
        if (email == null || !Boolean.TRUE.equals(session.getAttribute("forgotVerified"))) {
            response.sendRedirect(request.getContextPath() + "/forgot-password"); return;
        }
        String password = request.getParameter("password");
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
