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

    /**
     * Constructor private ngăn tạo đối tượng; các chức năng của lớp tiện ích được gọi trực tiếp qua phương
     * thức static.
     */
    private ImageUtil() { }

    /**
     * Được controller thêm/sửa danh mục gọi; chuyển Part cho save với thư mục category. Trả đường dẫn
     * category/tên-tệp để lưu vào Category.icon, hoặc null nếu không có ảnh.
     */
    public static String saveCategoryIcon(ServletContext context, Part part) throws IOException {
        return save(context, part, "category");
    }

    /**
     * Được controller thêm/sửa sản phẩm gọi; chuyển Part cho save với thư mục product. Trả đường dẫn để lưu
     * vào Product.image, hoặc null nếu không có ảnh; controller thêm yêu cầu ảnh, service sửa giữ ảnh cũ.
     */
    public static String saveProductImage(ServletContext context, Part part) throws IOException {
        return save(context, part, "product");
    }

    /**
     * Hàm lưu ảnh dùng chung: bỏ qua Part rỗng, kiểm tra MIME khai báo và phần mở rộng cho phép, tạo thư mục
     * /images/{folder} rồi ghi tệp với tên UUID. Trả folder/tên-tệp để lưu trong entity và phục vụ qua
     * DownloadImageController; dữ liệu sai ném IllegalArgumentException, lỗi đường dẫn/ghi tệp ném
     * IOException.
     */
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

    /**
     * Được ProfileController gọi khi cập nhật hồ sơ; trả null nếu không có tệp, kiểm tra MIME image/* rồi gọi
     * save với thư mục user. Controller giữ ảnh cũ khi nhận null, hoặc chuyển đường dẫn mới cho
     * UserService.updateProfile.
     */
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
