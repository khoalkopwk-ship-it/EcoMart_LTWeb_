package vn.iotstar.controllers;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.entity.Category;
import vn.iotstar.models.CategoryService;
import vn.iotstar.models.CategoryServiceImpl;

@WebServlet(urlPatterns = "/admin/category/list")
public class CategoryListController extends HttpServlet {

    private final CategoryService categoryService = new CategoryServiceImpl();

    /**
     * Lấy keyword và gọi CategoryService.search; service trả toàn bộ danh mục nếu từ khóa trống. Từ khóa quá
     * dài được đổi thành thông báo validation và danh sách đầy đủ. Cuối cùng đặt cateList/keyword vào request
     * rồi include list-category.jsp để SiteMesh thu và trang trí nội dung.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        List<Category> categoryList;
        try {
            categoryList = categoryService.search(keyword);
        } catch (IllegalArgumentException exception) {
            request.setAttribute("error", exception.getMessage());
            categoryList = categoryService.getAll();
        }

        request.setAttribute("cateList", categoryList);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/views/admin/list-category.jsp")
                .include(request, response);
    }
}
