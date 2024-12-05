package com.example.foodordersystem.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final HttpSession httpSession;

    public AuthInterceptor(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String email = (String) httpSession.getAttribute("email");

        if (email == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "로그인 필요");
            return false; // 인증되지 않은 경우 요청 차단
        }
        return true; // 인증된 경우 요청 허용

    }
}
