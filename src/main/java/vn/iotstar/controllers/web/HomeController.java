package vn.iotstar.controllers.web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.models.*;

@WebServlet("/home")
public class HomeController extends HttpServlet {
    private final ProductService productService = new ProductServiceImpl();
    private final CategoryService categoryService = new CategoryServiceImpl();
    /**
     * Gọi ProductService.getTop10Newest và CategoryService.getAll, đặt products và categories vào request rồi
     * include home.jsp để SiteMesh thu nội dung và hiển thị trang chủ qua decorator.
     */
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("products", productService.getTop10Newest());
        request.setAttribute("categories", categoryService.getAll());
        request.getRequestDispatcher("/views/web/home.jsp").include(request, response);
    }
}
