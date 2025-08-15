package com.blog.blogbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // tells Spring that this is a config class
public class CorsConfig {

    @Bean // tells Spring to run this method and use the returned object as a bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // applies to all routes (e.g., /api/v1/categories)
                        .allowedOrigins("http://localhost:3000", "https://blog-frontend-green-five.vercel.app/") // only allow this frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // which HTTP methods are allowed
                        .allowedHeaders("*"); // allow all headers (like Authorization, Content-Type, etc.)
            }
        };
    }
}
