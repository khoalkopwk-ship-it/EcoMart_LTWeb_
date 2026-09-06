package vn.iotstar.config;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;

/** Chặn tập trung mọi request tới /admin/* nếu session hiện tại không thuộc ADMIN. */
public class AdminAuthorizationFilter implements Filter {
    /** Khách được chuyển tới login; USER đã đăng nhập nhận HTTP 403 trước khi tới controller quản trị. */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        Object account = session == null ? null : session.getAttribute("account");
        if (!(account instanceof User user)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (!AuthorizationUtil.isAdmin(user)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Tài khoản không có quyền truy cập khu vực quản trị");
            return;
        }
        chain.doFilter(request, response);
    }
}
