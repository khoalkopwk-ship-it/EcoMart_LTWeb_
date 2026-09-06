package vn.iotstar.utils;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import vn.iotstar.models.UserServiceImpl;

/** Giải phóng connection pool của Hibernate khi Tomcat dừng ứng dụng. */
@WebListener
public class JpaContextListener implements ServletContextListener {

    /**
     * Callback Servlet container gọi khi ứng dụng khởi động; gọi JpaConfig.isOpen để khởi tạo factory sớm và
     * phát hiện lỗi cấu hình/kết nối ngay lúc triển khai, sau đó tạo hoặc đồng bộ tài khoản ADMIN cố định.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Khởi tạo sớm để lỗi persistence.xml/kết nối xuất hiện ngay khi deploy.
        JpaConfig.isOpen();
        new UserServiceImpl().ensureFixedAdmin();
    }

    /**
     * Callback Servlet container gọi khi ứng dụng dừng; gọi JpaConfig.close để đóng factory và giải phóng tài
     * nguyên kết nối của Hibernate.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JpaConfig.close();
    }
}
