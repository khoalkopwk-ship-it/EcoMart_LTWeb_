package vn.iotstar.controllers.auth;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.User;
import vn.iotstar.models.UserService;
import vn.iotstar.models.UserServiceImpl;
import vn.iotstar.utils.OtpUtil;

@WebServlet("/verify-otp")
public class VerifyOtpController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/auth/verify-otp.jsp").forward(request, response);
    }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("pendingUser") == null) {
            response.sendRedirect(request.getContextPath() + "/register"); return;
        }
        String expected = (String) session.getAttribute("registerOtp");
        Long created = (Long) session.getAttribute("registerOtpTime");
        String input = request.getParameter("otp");
        if (expected == null || created == null || OtpUtil.isExpired(created)) {
            request.setAttribute("error", "Mã OTP đã hết hạn. Vui lòng đăng ký lại"); doGet(request, response); return;
        }
        if (input == null || !expected.equals(input.trim())) {
            request.setAttribute("error", "Mã OTP không chính xác"); doGet(request, response); return;
        }
        try {
            userService.register((User) session.getAttribute("pendingUser"));
        } catch (IllegalArgumentException exception) {
            request.setAttribute("error", exception.getMessage()); doGet(request, response); return;
        }
        session.removeAttribute("pendingUser"); session.removeAttribute("registerOtp");
        session.removeAttribute("registerOtpTime");
        session.setAttribute("success", "Kích hoạt tài khoản thành công. Bạn có thể đăng nhập");
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
