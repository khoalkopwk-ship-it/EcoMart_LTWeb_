package vn.iotstar.controllers.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.User;
import vn.iotstar.models.UserService;
import vn.iotstar.models.UserServiceImpl;
import vn.iotstar.utils.ImageUtil;

import java.io.IOException;

@WebServlet("/profile")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024
)
public class ProfileController extends HttpServlet {

    private final UserService userService =
            new UserServiceImpl();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        User account = getAccount(request);

        if (account == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login");
            return;
        }

        User profile = userService.findById(account.getId());

        if (profile == null) {
            request.getSession().invalidate();
            response.sendRedirect(
                    request.getContextPath() + "/login");
            return;
        }

        request.getSession().setAttribute("account", profile);
        render(request, response, profile);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        User account = getAccount(request);

        if (account == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login");
            return;
        }

        try {
            String fullname =
                    request.getParameter("fullname");

            String phone =
                    request.getParameter("phone");

            String images = account.getImages();
            Part imagePart = request.getPart("image");

            String uploadedImage =
                    ImageUtil.saveUserImage(
                            getServletContext(), imagePart);

            if (uploadedImage != null) {
                images = uploadedImage;
            }

            User updated = userService.updateProfile(
                    account.getId(),
                    fullname,
                    phone,
                    images
            );

            request.getSession()
                    .setAttribute("account", updated);

            response.sendRedirect(
                    request.getContextPath()
                            + "/profile?updated=1");

        } catch (IllegalArgumentException |
                 IllegalStateException exception) {

            request.setAttribute(
                    "error", exception.getMessage());

            render(request, response, account);
        }
    }

    private User getAccount(HttpServletRequest request) {
        HttpSession session =
                request.getSession(false);

        if (session == null) {
            return null;
        }

        Object value = session.getAttribute("account");
        return value instanceof User ? (User) value : null;
    }

    private void render(
            HttpServletRequest request,
            HttpServletResponse response,
            User profile)
            throws ServletException, IOException {

        request.setAttribute("profile", profile);
        response.setContentType("text/html;charset=UTF-8");

        // Quan trọng với SiteMesh + Tomcat 11
        request.getRequestDispatcher(
                "/views/web/profile.jsp"
        ).include(request, response);
    }
}