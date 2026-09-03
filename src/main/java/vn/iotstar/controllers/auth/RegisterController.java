package vn.iotstar.controllers.auth;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.User;
import vn.iotstar.models.UserService;
import vn.iotstar.models.UserServiceImpl;
import vn.iotstar.utils.EmailUtil;
import vn.iotstar.utils.OtpUtil;

@WebServlet("/register")
public class RegisterController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
    }

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String fullname = trim(request.getParameter("fullname"));
        String username = trim(request.getParameter("username"));
        String email = trim(request.getParameter("email"));
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirmPassword");
        request.setAttribute("fullname", fullname);
        request.setAttribute("username", username);
        request.setAttribute("email", email);

        if (fullname.isBlank() || username.isBlank() || email.isBlank()
                || password == null || password.length() < 6) {
            error(request, response, "Vui lòng nhập đủ thông tin; mật khẩu tối thiểu 6 ký tự"); return;
        }
        if (!password.equals(confirm)) { error(request, response, "Mật khẩu xác nhận không khớp"); return; }
        if (userService.existsUsername(username)) { error(request, response, "Tên đăng nhập đã tồn tại"); return; }
        if (userService.existsEmail(email)) { error(request, response, "Email đã được sử dụng"); return; }

        User pending = new User();
        pending.setFullname(fullname); pending.setUsername(username);
        pending.setEmail(email); pending.setPassword(password);
        String otp = OtpUtil.generate();
        try {
            EmailUtil.sendOtp(email, otp, "kích hoạt tài khoản");
        } catch (Exception exception) {
            getServletContext().log("Không thể gửi OTP đăng ký", exception);
            error(request, response,
                    "Không gửi được OTP. Vui lòng kiểm tra cấu hình email");
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("pendingUser", pending);
        session.setAttribute("registerOtp", otp);
        session.setAttribute("registerOtpTime", System.currentTimeMillis());
        response.sendRedirect(request.getContextPath() + "/verify-otp");
    }

    private void error(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("error", message); doGet(request, response);
    }
    private String trim(String value) { return value == null ? "" : value.trim(); }
}
