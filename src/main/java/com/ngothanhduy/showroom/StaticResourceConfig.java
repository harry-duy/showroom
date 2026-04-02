package com.ngothanhduy.showroom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Value("${uploadsFolder}")
    private String uploadsFolder;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Dùng absolute path thay vì relative
        String absolutePath = Path.of(uploadsFolder)
                .toAbsolutePath()  // thêm toAbsolutePath()
                .toUri()
                .toString();

        registry
                .addResourceHandler("/files/**")
                .addResourceLocations(absolutePath)
                .setCacheControl(CacheControl.noCache());
    }
}