package com.hoainam.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Lấy đường dẫn gốc của dự án
        String projectDir = System.getProperty("user.dir");
        
        // Tạo đường dẫn tuyệt đối tới thư mục uploads trong src
        // Cấu trúc: file:/ + Đường dẫn dự án + Đường dẫn tới thư mục uploads
        String uploadPath = "file:/" + projectDir + "/src/main/resources/static/uploads/";

        // Map đường dẫn URL /uploads/** vào thư mục vật lý đó
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);
                
        System.out.println("DEBUG: Upload Path is: " + uploadPath);
    }
}