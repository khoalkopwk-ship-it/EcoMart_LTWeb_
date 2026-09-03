package vn.iotstar.controllers;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.models.CategoryService;
import vn.iotstar.models.CategoryServiceImpl;

@WebServlet(urlPatterns = "/admin/category/delete")
public class CategoryDeleteController extends HttpServlet {

    private final CategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            if (!categoryService.delete(id)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Không tìm thấy danh mục");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/admin/category/list");
        } catch (NumberFormatException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        } catch (RuntimeException exception) {
            request.getSession().setAttribute("categoryError",
                    "Không thể xóa danh mục đang được sản phẩm sử dụng");
            response.sendRedirect(request.getContextPath() + "/admin/category/list");
        }
    }
}
