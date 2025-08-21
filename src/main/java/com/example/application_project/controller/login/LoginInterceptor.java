package com.example.application_project.controller.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        // 로그인 페이지 접근 가능
        if (request.getRequestURI().contains("/")) {
            return true;
        }

        // 세션에 ID값 있으면 접근 가능
        if (request.getSession().getAttribute("loginId") != null) {
            return true;
        }

        // 로그인 안 되어 있으면 로그인 페이지로 리다이렉트
        response.sendRedirect("/");
        return false;
    }
}