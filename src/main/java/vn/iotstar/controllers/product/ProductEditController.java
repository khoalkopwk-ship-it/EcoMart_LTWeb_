package vn.iotstar.controllers.product;

import java.io.IOException;
import java.math.BigDecimal;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.*;
import vn.iotstar.models.*;
import vn.iotstar.utils.ImageUtil;

@WebServlet("/admin/product/edit")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 7 * 1024 * 1024)
public class ProductEditController extends HttpServlet {
    private final ProductService productService = new ProductServiceImpl();
    private final CategoryService categoryService = new CategoryServiceImpl();
    /**
     * Lấy sản phẩm bằng ProductService.get và danh mục bằng CategoryService.getAll rồi include
     * edit-product.jsp. Id sai định dạng trả HTTP 400; sản phẩm không tồn tại trả HTTP 404.
     */
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Product product = productService.get(Integer.parseInt(request.getParameter("id")));
            if (product == null) { response.sendError(404, "Không tìm thấy sản phẩm"); return; }
            request.setAttribute("product", product);
            request.setAttribute("categories", categoryService.getAll());
            request.getRequestDispatcher("/views/admin/product/edit-product.jsp").include(request, response);
        } catch (NumberFormatException exception) { response.sendError(400, "ID không hợp lệ"); }
    }
    /**
     * Dựng Product mang id cần sửa từ biểu mẫu, kiểm tra danh mục qua CategoryService và lưu ảnh bằng
     * ImageUtil. Gọi ProductService.edit để cập nhật dữ liệu, giữ ảnh cũ nếu không có ảnh mới, rồi chuyển danh
     * sách. Lỗi dữ liệu tải lại sản phẩm và danh mục để hiện form; id sai trả HTTP 400.
     */
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
            Product product = new Product();
            product.setId(id); product.setName(request.getParameter("name"));
            product.setPrice(new BigDecimal(request.getParameter("price")));
            product.setDescription(request.getParameter("description"));
            Category category = categoryService.get(Integer.parseInt(request.getParameter("categoryId")));
            if (category == null) throw new IllegalArgumentException("Danh mục không tồn tại");
            product.setCategory(category);
            product.setImage(ImageUtil.saveProductImage(getServletContext(), request.getPart("image")));
            productService.edit(product);
            response.sendRedirect(request.getContextPath() + "/admin/product/list");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            request.setAttribute("error", exception.getMessage());
            try { id = Integer.parseInt(request.getParameter("id")); }
            catch (NumberFormatException ignored) { response.sendError(400, "ID không hợp lệ"); return; }
            request.setAttribute("product", productService.get(id));
            request.setAttribute("categories", categoryService.getAll());
            preserve(request);
            request.getRequestDispatcher("/views/admin/product/edit-product.jsp").include(request, response);
        } catch (ServletException exception) {
            request.setAttribute("error", "Ảnh tải lên không hợp lệ hoặc vượt quá 5 MB");
            try { id = Integer.parseInt(request.getParameter("id")); }
            catch (NumberFormatException ignored) { response.sendError(400, "ID không hợp lệ"); return; }
            request.setAttribute("product", productService.get(id));
            request.setAttribute("categories", categoryService.getAll());
            preserve(request);
            request.getRequestDispatcher("/views/admin/product/edit-product.jsp").include(request, response);
        }
    }

    /**
     * Giữ các giá trị người dùng vừa nhập trong request khi validation thất bại. edit-product.jsp ưu tiên
     * nhóm entered* này thay vì dữ liệu cũ vừa tải lại từ database.
     */
    private void preserve(HttpServletRequest request) {
        request.setAttribute("hasSubmittedValues", true);
        request.setAttribute("enteredName", request.getParameter("name"));
        request.setAttribute("enteredPrice", request.getParameter("price"));
        request.setAttribute("enteredDescription", request.getParameter("description"));
        request.setAttribute("enteredCategory", request.getParameter("categoryId"));
    }
}
