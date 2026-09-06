package vn.iotstar.controllers.auth;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.User;
import vn.iotstar.models.UserService;
import vn.iotstar.models.UserServiceImpl;
import vn.iotstar.utils.FormValidationUtil;
import vn.iotstar.utils.OtpUtil;

@WebServlet("/verify-otp")
public class VerifyOtpController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();
    /**
     * Include verify-otp.jsp để SiteMesh thu form nhập mã; doPost gọi lại khi OTP hoặc dữ liệu đăng ký không hợp
     * lệ.
     */
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/auth/verify-otp.jsp").include(request, response);
    }
    /**
     * Lấy pendingUser và OTP do RegisterController lưu trong session; thiếu user thì chuyển về đăng ký. Kiểm
     * tra hạn bằng OtpUtil.isExpired và đối chiếu mã; hợp lệ mới gọi UserService.register để băm mật khẩu và
     * lưu qua repository. Xóa dữ liệu đăng ký tạm, đặt thông báo thành công và chuyển /login; lỗi được hiển
     * thị lại qua doGet.
     */
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
        if (!FormValidationUtil.isValidOtp(input) || !expected.equals(input.trim())) {
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
