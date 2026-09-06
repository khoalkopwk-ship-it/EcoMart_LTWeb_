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

@WebServlet("/admin/product/add")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 7 * 1024 * 1024)
public class ProductAddController extends HttpServlet {
    private final ProductService productService = new ProductServiceImpl();
    private final CategoryService categoryService = new CategoryServiceImpl();
    /**
     * Gọi CategoryService.getAll và đặt categories vào request để add-product.jsp hiển thị lựa chọn danh mục;
     * doPost gọi lại khi nhập sai.
     */
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("categories", categoryService.getAll());
        request.getRequestDispatcher("/views/admin/product/add-product.jsp").include(request, response);
    }
    /**
     * Dựng Product từ biểu mẫu, chuyển giá sang BigDecimal, lấy danh mục qua CategoryService.get và yêu cầu
     * ảnh được ImageUtil.saveProductImage lưu thành công. Gọi ProductService.insert để kiểm tra và lưu qua
     * repository rồi chuyển danh sách; lỗi dữ liệu gọi preserve và doGet để hiện lại form.
     */
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            Product product = new Product();
            product.setName(request.getParameter("name"));
            product.setPrice(new BigDecimal(request.getParameter("price")));
            product.setDescription(request.getParameter("description"));
            Category category = categoryService.get(Integer.parseInt(request.getParameter("categoryId")));
            if (category == null) throw new IllegalArgumentException("Danh mục không tồn tại");
            product.setCategory(category);
            String imagePath = ImageUtil.saveProductImage(getServletContext(), request.getPart("image"));
            if (imagePath == null) throw new IllegalArgumentException("Vui lòng chọn ảnh sản phẩm");
            product.setImage(imagePath);
            productService.insert(product);
            response.sendRedirect(request.getContextPath() + "/admin/product/list");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            request.setAttribute("error", exception.getMessage()); preserve(request); doGet(request, response);
        } catch (ServletException exception) {
            request.setAttribute("error", "Ảnh tải lên không hợp lệ hoặc vượt quá 5 MB");
            preserve(request); doGet(request, response);
        }
    }
    /**
     * Sao chép tên, giá, mô tả và mã danh mục từ tham số sang các thuộc tính entered* trong request; doPost
     * dùng trước doGet để JSP giữ dữ liệu khi thêm sản phẩm thất bại.
     */
    private void preserve(HttpServletRequest request) {
        request.setAttribute("enteredName", request.getParameter("name"));
        request.setAttribute("enteredPrice", request.getParameter("price"));
        request.setAttribute("enteredDescription", request.getParameter("description"));
        request.setAttribute("enteredCategory", request.getParameter("categoryId"));
    }
}
