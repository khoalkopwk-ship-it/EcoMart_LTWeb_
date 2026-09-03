package vn.iotstar.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/image")
public class DownloadImageController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fileName = request.getParameter("fname");
        String imageRoot = getServletContext().getRealPath("/images");

        if (fileName == null || fileName.isBlank() || imageRoot == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Path root = Path.of(imageRoot).toAbsolutePath().normalize();
        Path file = root.resolve(fileName).normalize();

        if (!file.startsWith(root)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        if (!Files.isRegularFile(file)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String contentType = getServletContext().getMimeType(file.getFileName().toString());
        response.setContentType(contentType != null ? contentType : "application/octet-stream");
        Files.copy(file, response.getOutputStream());
    }
}
