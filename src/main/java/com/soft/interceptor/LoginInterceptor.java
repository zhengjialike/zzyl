package com.soft.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soft.common.Result;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            return true;
        }
        response.setContentType("application/json;charset=utf-8");
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(Result.error(401, "未登录")));
        return false;
    }
}
