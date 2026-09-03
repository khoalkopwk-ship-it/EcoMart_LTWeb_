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
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class CategoryEditController extends HttpServlet {

    private final CategoryService categoryService = new CategoryServiceImpl();

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
                    .forward(request, response);
        } catch (NumberFormatException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            int status = "0".equals(request.getParameter("status")) ? 0 : 1;
            Part iconPart = request.getPart("icon");
            String iconPath = ImageUtil.saveCategoryIcon(getServletContext(), iconPart);

            categoryService.edit(new Category(id, name, iconPath, status));
            response.sendRedirect(request.getContextPath() + "/admin/category/list");
        } catch (IllegalArgumentException exception) {
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
                    .forward(request, response);
        }
    }
}
