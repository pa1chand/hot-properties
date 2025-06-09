// src/main/java/edu/depaul/hot_properties/config/WebConfig.java
package edu.depaul.hot_properties.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class to explicitly map static resources and the file upload directory
 * as resource handlers, allowing them to be served via HTTP.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Injects the 'upload.dir' property from application.properties
    // This property specifies the file system directory where uploaded images are stored.
    @Value("${upload.dir}")
    private String uploadDir;

    /**
     * Adds resource handlers to serve static content from specified locations.
     *
     * @param registry The ResourceHandlerRegistry to configure.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Handler for CSS files: maps /css/** requests to files in src/main/resources/static/css/
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");

        // Handler for pre-bundled images: maps /images/** requests to files in src/main/resources/static/images/
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");

        // Handler for dynamically uploaded property images:
        // Maps /uploads/** requests to the file system directory specified by 'upload.dir'
        // The 'file:' prefix indicates a file system resource. It's crucial that 'uploadDir'
        // correctly points to the absolute path where your images are saved.
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }
}