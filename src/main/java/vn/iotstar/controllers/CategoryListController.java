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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        List<Category> categoryList = categoryService.search(keyword);

        request.setAttribute("cateList", categoryList);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/views/admin/list-category.jsp")
                .forward(request, response);
    }
}
