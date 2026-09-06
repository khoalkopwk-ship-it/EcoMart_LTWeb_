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

/** Yêu cầu một session đăng nhập hợp lệ cho các trang riêng của người dùng. */
public class UserAuthenticationFilter implements Filter {
    /** Chuyển khách chưa đăng nhập tới form login; USER hoặc ADMIN hợp lệ được đi tiếp. */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        if (session == null || !(session.getAttribute("account") instanceof User)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        chain.doFilter(request, response);
    }
}
