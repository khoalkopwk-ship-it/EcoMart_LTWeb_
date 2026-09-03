package vn.iotstar.config;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.sitemesh.webapp.DispatchMode;

public class EcoMartSiteMeshFilter extends ConfigurableSiteMeshFilter {

    @Override
    protected void applyCustomConfiguration(
            SiteMeshFilterBuilder builder) {

        builder
                .setDecoratorPrefix("/WEB-INF/decorators/")
                .addDecoratorPath("/profile", "main.jsp")
                .setMimeTypes("text/html")
                .setDispatchMode(DispatchMode.INCLUDE);
    }
}