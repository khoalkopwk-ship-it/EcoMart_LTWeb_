package vn.iotstar.controllers.web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.models.*;

@WebServlet("/product")
public class ProductListController extends HttpServlet {
    private final ProductService productService = new ProductServiceImpl();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int page = parsePage(request.getParameter("page"));
        int pageSize = 6;
        long total = productService.count();
        int totalPages = (int) Math.ceil((double) total / pageSize);
        if (totalPages > 0 && page > totalPages) page = totalPages;
        request.setAttribute("products", productService.getPage(page, pageSize));
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalProducts", total);
        request.getRequestDispatcher("/views/web/product-list.jsp").forward(request, response);
    }
    private int parsePage(String value) {
        try { return Math.max(1, Integer.parseInt(value)); }
        catch (NumberFormatException exception) { return 1; }
    }
}
