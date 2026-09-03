package vn.iotstar.controllers.product;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.models.ProductService;
import vn.iotstar.models.ProductServiceImpl;

@WebServlet("/admin/product/list")
public class ProductAdminListController extends HttpServlet {
    private final ProductService productService = new ProductServiceImpl();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("products", productService.getAll());
        request.getRequestDispatcher("/views/admin/product/list-product.jsp").forward(request, response);
    }
}
