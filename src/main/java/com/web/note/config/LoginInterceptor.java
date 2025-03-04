package com.web.note.config;

import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.web.note.constant.PropertiesName.USER_NAME;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (request.getSession().getAttribute(USER_NAME) == null) {
            String contextPath = request.getContextPath();
            response.sendRedirect(contextPath + "/login.html");
            return false;
        }
        return true;
    }

}
