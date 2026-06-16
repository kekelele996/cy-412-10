package com.smartestate.config;

import com.smartestate.interceptor.JwtInterceptor;
import com.smartestate.interceptor.OperationLogInterceptor;
import com.smartestate.interceptor.RateLimitInterceptor;
import com.smartestate.interceptor.RbacInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {
    private final JwtInterceptor jwtInterceptor;
    private final RbacInterceptor rbacInterceptor;
    private final OperationLogInterceptor operationLogInterceptor;
    private final RateLimitInterceptor rateLimitInterceptor;

    public SecurityConfig(JwtInterceptor jwtInterceptor, RbacInterceptor rbacInterceptor,
                          OperationLogInterceptor operationLogInterceptor, RateLimitInterceptor rateLimitInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
        this.rbacInterceptor = rbacInterceptor;
        this.operationLogInterceptor = operationLogInterceptor;
        this.rateLimitInterceptor = rateLimitInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor).addPathPatterns("/**");
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/health", "/auth/login");
        registry.addInterceptor(rbacInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/health", "/auth/login");
        registry.addInterceptor(operationLogInterceptor).addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*").allowedHeaders("*").allowedOrigins("*");
    }
}
