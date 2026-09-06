package vn.iotstar.controllers.web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.models.*;

@WebServlet("/product")
public class ProductListController extends HttpServlet {
    private final ProductService productService = new ProductServiceImpl();
    /**
     * Dùng parsePage để chuẩn hóa trang, gọi ProductService.count để tính tổng số trang với 6 sản phẩm mỗi
     * trang và giới hạn trang vượt cuối. Lấy dữ liệu qua getPage, đặt danh sách cùng thông tin phân trang vào
     * request rồi include product-list.jsp để SiteMesh thu nội dung.
     */
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
        request.getRequestDispatcher("/views/web/product-list.jsp").include(request, response);
    }
    /**
     * Chuyển tham số page sang số nguyên tối thiểu 1; thiếu hoặc sai định dạng thì trả 1. doGet dùng kết quả
     * trước khi giới hạn theo tổng số trang.
     */
    private int parsePage(String value) {
        try { return Math.max(1, Integer.parseInt(value)); }
        catch (NumberFormatException exception) { return 1; }
    }
}
