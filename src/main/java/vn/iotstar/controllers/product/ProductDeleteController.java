package vn.iotstar.controllers.product;

import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.models.ProductService;
import vn.iotstar.models.ProductServiceImpl;

@WebServlet("/admin/product/delete")
public class ProductDeleteController extends HttpServlet {
    private final ProductService productService = new ProductServiceImpl();
    /**
     * Đọc id và gọi ProductService.delete để repository xóa sản phẩm trong transaction, sau đó chuyển về danh
     * sách. Trả HTTP 400 nếu id không phải số; kết quả boolean của service không được kiểm tra nên id không
     * tồn tại cũng chuyển về danh sách.
     */
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try { productService.delete(Integer.parseInt(request.getParameter("id"))); }
        catch (NumberFormatException ignored) { response.sendError(400, "ID không hợp lệ"); return; }
        response.sendRedirect(request.getContextPath() + "/admin/product/list");
    }
}
