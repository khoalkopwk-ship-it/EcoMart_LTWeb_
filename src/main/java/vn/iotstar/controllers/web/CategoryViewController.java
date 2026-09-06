package vn.iotstar.controllers.web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.models.CategoryService;
import vn.iotstar.models.CategoryServiceImpl;

/** Hiển thị danh mục ở chế độ chỉ đọc cho tài khoản đã đăng nhập. */
@WebServlet("/category")
public class CategoryViewController extends HttpServlet {
    private final CategoryService categoryService = new CategoryServiceImpl();

    /** Lấy danh mục từ service hiện có rồi include JSP để SiteMesh áp dụng layout cửa hàng. */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("categories", categoryService.getAll());
        request.getRequestDispatcher("/views/web/category-list.jsp").include(request, response);
    }
}
