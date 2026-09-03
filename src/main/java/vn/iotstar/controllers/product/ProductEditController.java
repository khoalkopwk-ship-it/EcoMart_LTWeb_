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
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Product product = productService.get(Integer.parseInt(request.getParameter("id")));
            if (product == null) { response.sendError(404, "Không tìm thấy sản phẩm"); return; }
            request.setAttribute("product", product);
            request.setAttribute("categories", categoryService.getAll());
            request.getRequestDispatcher("/views/admin/product/edit-product.jsp").forward(request, response);
        } catch (NumberFormatException exception) { response.sendError(400, "ID không hợp lệ"); }
    }
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
        } catch (IllegalArgumentException exception) {
            request.setAttribute("error", exception.getMessage());
            try { id = Integer.parseInt(request.getParameter("id")); }
            catch (NumberFormatException ignored) { response.sendError(400, "ID không hợp lệ"); return; }
            request.setAttribute("product", productService.get(id));
            request.setAttribute("categories", categoryService.getAll());
            request.getRequestDispatcher("/views/admin/product/edit-product.jsp").forward(request, response);
        }
    }
}
