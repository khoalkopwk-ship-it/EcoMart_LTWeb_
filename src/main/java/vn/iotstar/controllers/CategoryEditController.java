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

@WebServlet(urlPatterns = "/admin/category/edit")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 6 * 1024 * 1024)
public class CategoryEditController extends HttpServlet {

    private final CategoryService categoryService = new CategoryServiceImpl();

    /**
     * Đọc id, gọi CategoryService.get để lấy danh mục và đưa vào request cho edit-category.jsp. Trả HTTP 400
     * nếu id sai định dạng, HTTP 404 nếu không tìm thấy.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Category category = categoryService.get(id);
            if (category == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Không tìm thấy danh mục");
                return;
            }

            request.setAttribute("category", category);
            request.getRequestDispatcher("/views/admin/edit-category.jsp")
                    .include(request, response);
        } catch (NumberFormatException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        }
    }

    /**
     * Đọc dữ liệu sửa và lưu ảnh mới qua ImageUtil, sau đó gọi CategoryService.edit; service giữ ảnh cũ khi
     * không tải ảnh mới. Thành công chuyển về danh sách; lỗi dữ liệu dựng lại danh mục từ biểu mẫu và ảnh cũ
     * để JSP hiển thị, hoặc trả HTTP 400 nếu id sai.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            int status = parseStatus(request.getParameter("status"));
            Part iconPart = request.getPart("icon");
            String iconPath = ImageUtil.saveCategoryIcon(getServletContext(), iconPart);

            categoryService.edit(new Category(id, name, iconPath, status));
            response.sendRedirect(request.getContextPath() + "/admin/category/list");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            Category category = new Category();
            try {
                category.setId(Integer.parseInt(request.getParameter("id")));
            } catch (NumberFormatException ignored) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
                return;
            }
            category.setName(request.getParameter("name"));
            category.setStatus("0".equals(request.getParameter("status")) ? 0 : 1);
            Category oldCategory = categoryService.get(category.getId());
            if (oldCategory != null) {
                category.setIcon(oldCategory.getIcon());
            }

            request.setAttribute("category", category);
            request.setAttribute("error", exception.getMessage());
            request.getRequestDispatcher("/views/admin/edit-category.jsp")
                    .include(request, response);
        } catch (ServletException exception) {
            request.setAttribute("error", "Ảnh tải lên không hợp lệ hoặc vượt quá 5 MB");
            doGet(request, response);
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
