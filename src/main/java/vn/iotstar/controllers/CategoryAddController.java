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
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class CategoryAddController extends HttpServlet {

    private final CategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/admin/add-category.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            String name = request.getParameter("name");
            int status = "0".equals(request.getParameter("status")) ? 0 : 1;
            Part iconPart = request.getPart("icon");
            String iconPath = ImageUtil.saveCategoryIcon(getServletContext(), iconPart);

            categoryService.insert(new Category(name, iconPath, status));
            response.sendRedirect(request.getContextPath() + "/admin/category/list");
        } catch (IllegalArgumentException exception) {
            request.setAttribute("error", exception.getMessage());
            request.setAttribute("enteredName", request.getParameter("name"));
            request.setAttribute("enteredStatus", request.getParameter("status"));
            request.getRequestDispatcher("/views/admin/add-category.jsp")
                    .forward(request, response);
        }
    }
}
