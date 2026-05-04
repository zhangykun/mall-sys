package com.mall.mallsys.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final FileUploadProperties uploadProperties;

    public WebMvcConfig(FileUploadProperties uploadProperties) {
        this.uploadProperties = uploadProperties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = uploadProperties.getPath();
        if (!path.endsWith("/")) {
            path += "/";
        }
        registry.addResourceHandler(uploadProperties.getPrefix() + "/**")
                .addResourceLocations("file:" + path);
    }
}
