package com.gymmaster.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
      // allow cross regin requests from all domains
        registry.addMapping("/**")
                // set the domain name that is allowed to access, * means all domains
                .allowedOriginPatterns("*")
                //  allow cookies to be passed from the front end
                .allowCredentials(true)
                // allowed methods
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                // allowed headers
                .allowedHeaders("*")
                // set the cache time for preflight requests
                .maxAge(3600);
    }
}
