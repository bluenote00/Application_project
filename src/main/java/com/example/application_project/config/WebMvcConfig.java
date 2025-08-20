package com.example.application_project.config;

import com.example.application_project.controller.login.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**") // 모든 요청에 인터셉터 적용
                .excludePathPatterns(
                        "/",
                        "/member/login",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/favicon.ico"
                );
    }
}
