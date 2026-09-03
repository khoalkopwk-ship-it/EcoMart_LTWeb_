package vn.iotstar.utils;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/** Giải phóng connection pool của Hibernate khi Tomcat dừng ứng dụng. */
@WebListener
public class JpaContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Khởi tạo sớm để lỗi persistence.xml/kết nối xuất hiện ngay khi deploy.
        JpaConfig.isOpen();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JpaConfig.close();
    }
}
