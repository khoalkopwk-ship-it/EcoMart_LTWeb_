package vn.iotstar.utils;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Part;

public final class ImageUtil {
    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of(".jpg", ".jpeg", ".png", ".gif", ".webp");

    private ImageUtil() { }

    public static String saveCategoryIcon(ServletContext context, Part part) throws IOException {
        return save(context, part, "category");
    }

    public static String saveProductImage(ServletContext context, Part part) throws IOException {
        return save(context, part, "product");
    }

    private static String save(
            ServletContext context,
            Part part,
            String folder) throws IOException {

        if (part == null
                || part.getSize() == 0
                || part.getSubmittedFileName() == null
                || part.getSubmittedFileName().isBlank()) {
            return null;
        }

        if (part.getContentType() == null
                || !part.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException(
                    "File được chọn không phải là hình ảnh"
            );
        }

        String originalName =
                new File(part.getSubmittedFileName()).getName();

        int dotIndex = originalName.lastIndexOf('.');
        String extension = dotIndex >= 0
                ? originalName.substring(dotIndex).toLowerCase()
                : "";

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException(
                    "Chỉ chấp nhận ảnh JPG, JPEG, PNG, GIF hoặc WEBP"
            );
        }

        String realPath =
                context.getRealPath("/images/" + folder);

        if (realPath == null) {
            throw new IOException(
                    "Tomcat không cung cấp đường dẫn lưu ảnh"
            );
        }

        File directory = new File(realPath);

        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException(
                    "Không thể tạo thư mục: " + realPath
            );
        }

        String fileName = UUID.randomUUID() + extension;

        part.write(
                new File(directory, fileName).getAbsolutePath()
        );

        return folder + "/" + fileName;
    }

    //đã có hàm lưu ảnh

    public static String saveUserImage(
            ServletContext context,
            Part part) throws IOException {

        if (part == null || part.getSize() == 0) {
            return null;
        }

        String contentType = part.getContentType();

        if (contentType == null ||
                !contentType.startsWith("image/")) {
            throw new IllegalArgumentException(
                    "Tệp được chọn không phải là hình ảnh");
        }

        return save(context, part, "user");
    }
}
