package vn.iotstar.controllers.auth;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.User;
import vn.iotstar.models.UserService;
import vn.iotstar.models.UserServiceImpl;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
    }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String login = request.getParameter("login");
        User user = userService.login(login, request.getParameter("password"));
        if (user == null) {
            request.setAttribute("error", "Tài khoản/email hoặc mật khẩu không đúng");
            request.setAttribute("login", login); doGet(request, response); return;
        }
        request.getSession().setAttribute("account", user);
        response.sendRedirect(request.getContextPath() + "/home");
    }
}
