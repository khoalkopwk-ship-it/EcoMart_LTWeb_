package vn.iotstar.controllers.auth;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.User;
import vn.iotstar.models.UserService;
import vn.iotstar.models.UserServiceImpl;
import vn.iotstar.utils.EmailUtil;
import vn.iotstar.utils.FormValidationUtil;
import vn.iotstar.utils.OtpUtil;

@WebServlet("/register")
public class RegisterController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();

    /**
     * Include register.jsp để SiteMesh thu nội dung; hàm error gọi lại doGet để hiển thị lỗi và dữ liệu cũ.
     */
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/auth/register.jsp").include(request, response);
    }

    /**
     * Chuẩn hóa thông tin bằng trim, kiểm tra trường bắt buộc, mật khẩu xác nhận và tài khoản trùng qua
     * UserService. Tạo User chờ đăng ký, sinh OTP bằng OtpUtil và gửi qua EmailUtil; sau khi gửi thành công
     * lưu pendingUser, registerOtp, registerOtpTime vào session rồi chuyển /verify-otp. VerifyOtpController
     * mới gọi service để lưu tài khoản sau khi mã hợp lệ.
     */
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

        if (fullname.isBlank() || fullname.length() > 255) {
            error(request, response, "Họ và tên không được để trống và tối đa 255 ký tự"); return;
        }
        if (!FormValidationUtil.isValidUsername(username)) {
            error(request, response, "Tên đăng nhập phải có 3–100 ký tự chữ, số, dấu chấm, _ hoặc -"); return;
        }
        if (!FormValidationUtil.isValidEmail(email)) {
            error(request, response, "Email không đúng định dạng hoặc dài quá 255 ký tự"); return;
        }
        if (!FormValidationUtil.isValidPassword(password)) {
            error(request, response, "Mật khẩu phải từ 6 đến 255 ký tự"); return;
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

    /**
     * Đặt thông báo error vào request rồi gọi doGet để hiện lại biểu mẫu đăng ký; được doPost dùng khi dữ liệu
     * hoặc việc gửi email thất bại.
     */
    private void error(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("error", message); doGet(request, response);
    }
    /**
     * Đổi giá trị null thành chuỗi rỗng, còn lại loại khoảng trắng đầu/cuối; doPost dùng để kiểm tra và giữ
     * lại họ tên, tên đăng nhập, email an toàn với null.
     */
    private String trim(String value) { return value == null ? "" : value.trim(); }
}
