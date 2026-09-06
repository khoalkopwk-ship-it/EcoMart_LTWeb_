package vn.iotstar.controllers.user;

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

    /**
     * Lấy account bằng getAccount; chưa đăng nhập thì chuyển /login. Tải hồ sơ mới nhất qua
     * UserService.findById, hủy session nếu tài khoản không còn tồn tại; ngược lại cập nhật session.account
     * rồi gọi render.
     */
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

    /**
     * Yêu cầu account trong session qua getAccount, đọc họ tên/số điện thoại và lưu ảnh qua
     * ImageUtil.saveUserImage; giữ đường dẫn ảnh cũ khi không tải ảnh mới. Gọi UserService.updateProfile rồi
     * cập nhật session.account và chuyển /profile?updated=1; lỗi dữ liệu hoặc trạng thái upload thì gọi render
     * với hồ sơ hiện có và thông báo.
     */
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
        } catch (ServletException exception) {
            request.setAttribute(
                    "error", "Ảnh tải lên không hợp lệ hoặc vượt quá 5 MB");
            render(request, response, account);
        }
    }

    /**
     * Đọc thuộc tính account từ session hiện có và chỉ trả về nếu là User; trả null nếu chưa có session hoặc
     * sai kiểu. doGet/doPost dùng để xác định tài khoản đang đăng nhập mà không tạo session mới.
     */
    private User getAccount(HttpServletRequest request) {
        HttpSession session =
                request.getSession(false);

        if (session == null) {
            return null;
        }

        Object value = session.getAttribute("account");
        return value instanceof User ? (User) value : null;
    }

    /**
     * Đặt profile vào request và include profile.jsp để EcoMartSiteMeshFilter thu response HTML rồi ghép vào
     * request bên ngoài và ghép vào Bootstrap decorator main.jsp; doGet/doPost dùng chung hàm này.
     */
    private void render(
            HttpServletRequest request,
            HttpServletResponse response,
            User profile)
            throws ServletException, IOException {

        request.setAttribute("profile", profile);
        response.setContentType("text/html;charset=UTF-8");

        request.getRequestDispatcher(
                "/views/web/profile.jsp"
        ).include(request, response);
    }
}
