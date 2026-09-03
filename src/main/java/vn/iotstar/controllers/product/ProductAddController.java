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
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("categories", categoryService.getAll());
        request.getRequestDispatcher("/views/admin/product/add-product.jsp").forward(request, response);
    }
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
        } catch (IllegalArgumentException exception) {
            request.setAttribute("error", exception.getMessage()); preserve(request); doGet(request, response);
        }
    }
    private void preserve(HttpServletRequest request) {
        request.setAttribute("enteredName", request.getParameter("name"));
        request.setAttribute("enteredPrice", request.getParameter("price"));
        request.setAttribute("enteredDescription", request.getParameter("description"));
        request.setAttribute("enteredCategory", request.getParameter("categoryId"));
    }
}
