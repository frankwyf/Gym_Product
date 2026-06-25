package com.gymmaster.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS configuration.
 *
 * <p>Allowed origins are read from the {@code GYMMASTER_CORS_ORIGINS} environment variable
 * (comma-separated) so that production deployments can restrict access without code changes.
 * If the variable is not set, {@code http://localhost:8080} (the default Vue dev server) is
 * used as a safe fallback — wildcard {@code *} is intentionally never the default.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origins:http://localhost:8080}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(allowedOrigins)
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("token")   // allow the frontend to read the token header
                .maxAge(3600);
    }
}
