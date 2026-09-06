package vn.iotstar.controllers.auth;

import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {
    /**
     * Hủy session xác thực hiện có rồi chuyển về /home. Cookie ECOMART_LAST_LOGIN được giữ nguyên để form
     * login nhớ định danh trong 24 giờ; cookie này không có khả năng tự đăng nhập.
     */
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        response.sendRedirect(request.getContextPath() + "/home");
    }
}
