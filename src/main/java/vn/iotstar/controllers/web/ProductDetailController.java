package vn.iotstar.controllers.web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.Product;
import vn.iotstar.models.*;

@WebServlet("/product/detail")
public class ProductDetailController extends HttpServlet {
    private final ProductService productService = new ProductServiceImpl();
    /**
     * Đọc id, gọi ProductService.get và đặt product vào request cho product-detail.jsp; quan hệ category của
     * Product được JPA tải EAGER. Trả HTTP 400 khi mã sai định dạng và HTTP 404 khi không có sản phẩm.
     */
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Product product = productService.get(Integer.parseInt(request.getParameter("id")));
            if (product == null) { response.sendError(404, "Không tìm thấy sản phẩm"); return; }
            request.setAttribute("product", product);
            request.getRequestDispatcher("/views/web/product-detail.jsp").include(request, response);
        } catch (NumberFormatException exception) {
            response.sendError(400, "Mã sản phẩm không hợp lệ");
        }
    }
}
