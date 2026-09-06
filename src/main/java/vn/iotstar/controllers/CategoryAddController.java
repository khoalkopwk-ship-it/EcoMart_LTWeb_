package vn.iotstar.controllers;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import vn.iotstar.entity.Category;
import vn.iotstar.models.CategoryService;
import vn.iotstar.models.CategoryServiceImpl;
import vn.iotstar.utils.ImageUtil;

@WebServlet(urlPatterns = "/admin/category/add")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 6 * 1024 * 1024)
public class CategoryAddController extends HttpServlet {

    private final CategoryService categoryService = new CategoryServiceImpl();

    /**
     * Nhận GET /admin/category/add và include add-category.jsp để SiteMesh thu nội dung biểu mẫu.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/admin/add-category.jsp")
                .include(request, response);
    }

    /**
     * Nhận tên, trạng thái và ảnh từ biểu mẫu; gọi ImageUtil.saveCategoryIcon rồi CategoryService.insert để
     * kiểm tra và lưu danh mục qua repository. Thành công chuyển về danh sách; lỗi dữ liệu trả lại biểu mẫu
     * kèm thông báo và giá trị đã nhập.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            String name = request.getParameter("name");
            int status = parseStatus(request.getParameter("status"));
            Part iconPart = request.getPart("icon");
            String iconPath = ImageUtil.saveCategoryIcon(getServletContext(), iconPart);

            categoryService.insert(new Category(name, iconPath, status));
            response.sendRedirect(request.getContextPath() + "/admin/category/list");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            request.setAttribute("error", exception.getMessage());
            request.setAttribute("enteredName", request.getParameter("name"));
            request.setAttribute("enteredStatus", request.getParameter("status"));
            request.getRequestDispatcher("/views/admin/add-category.jsp")
                    .include(request, response);
        } catch (ServletException exception) {
            request.setAttribute("error", "Ảnh tải lên không hợp lệ hoặc vượt quá 5 MB");
            request.setAttribute("enteredName", request.getParameter("name"));
            request.setAttribute("enteredStatus", request.getParameter("status"));
            request.getRequestDispatcher("/views/admin/add-category.jsp")
                    .include(request, response);
        }
    }

    /** Chỉ chấp nhận trạng thái 0 hoặc 1 từ form; giá trị khác bị chuyển thành lỗi validation cho doPost. */
    private int parseStatus(String value) {
        if (!"0".equals(value) && !"1".equals(value)) {
            throw new IllegalArgumentException("Trạng thái danh mục không hợp lệ");
        }
        return Integer.parseInt(value);
    }
}
