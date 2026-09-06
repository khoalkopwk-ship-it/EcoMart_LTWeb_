package vn.iotstar.controllers.auth;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.User;
import vn.iotstar.models.UserService;
import vn.iotstar.models.UserServiceImpl;
import vn.iotstar.entity.Role;
import vn.iotstar.utils.RememberLoginCookie;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();
    /**
     * Include login.jsp để SiteMesh thu biểu mẫu đăng nhập; doPost cũng gọi lại khi xác thực thất bại.
     */
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getAttribute("login") == null) {
            request.setAttribute("login", RememberLoginCookie.read(request));
        }
        request.getRequestDispatcher("/views/auth/login.jsp").include(request, response);
    }
    /**
     * Chuyển tài khoản/email và mật khẩu cho UserService.login để tìm user và kiểm tra mật khẩu. Nếu thất bại
     * giữ tên đăng nhập cùng thông báo rồi gọi doGet; nếu thành công lưu user vào session.account cho các
     * trang dùng chung, gia hạn cookie ghi nhớ và chuyển USER tới /home hoặc ADMIN tới trang quản trị.
     */
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String login = request.getParameter("login");
        User user = userService.login(login, request.getParameter("password"));
        if (user == null) {
            request.setAttribute("error", "Tài khoản/email hoặc mật khẩu không đúng");
            request.setAttribute("login", login); doGet(request, response); return;
        }
        request.getSession().setAttribute("account", user);
        RememberLoginCookie.refresh(request, response, user.getUsername());
        String destination = user.getRole() == Role.ADMIN ? "/admin/category/list" : "/home";
        response.sendRedirect(request.getContextPath() + destination);
    }
}
