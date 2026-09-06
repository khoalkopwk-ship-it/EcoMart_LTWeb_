package vn.iotstar.config;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.sitemesh.webapp.DispatchMode;

public class EcoMartSiteMeshFilter extends ConfigurableSiteMeshFilter {

    /**
     * Cấu hình một Bootstrap decorator cho mọi trang HTML của ứng dụng. Các tài nguyên tĩnh và endpoint trả
     * ảnh bị loại trừ vì chúng không phải nội dung HTML; SiteMesh chỉ ghép title, head và body của JSP vào
     * main.jsp. Chế độ INCLUDE tương thích với cách Tomcat 11 dispatch tới JSP decorator.
     */
    @Override
    protected void applyCustomConfiguration(
            SiteMeshFilterBuilder builder) {

        builder
                .setDecoratorPrefix("/WEB-INF/decorators/")
                .addDecoratorPath("/*", "main.jsp")
                .addExcludedPath("/assets/*")
                .addExcludedPath("/images/*")
                .addExcludedPath("/image")
                .setMimeTypes("text/html")
                .setDispatchMode(DispatchMode.INCLUDE);
    }
}
