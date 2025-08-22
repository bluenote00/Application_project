package com.example.application_project.controller.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String uri = request.getRequestURI();

        // 1. 로그인 페이지와 로그인 요청은 무조건 허용
        if (uri.equals("/") || uri.equals("/member/login")) {
            return true;
        }

        // 2. 세션 체크 (로그인 성공 여부 확인)
        if (request.getSession().getAttribute("loginId") != null) {
            return true;
        }

        // 3. 로그인 안되어 있으면 로그인 페이지로 리다이렉트
        response.sendRedirect("/");
        return false;
    }

}