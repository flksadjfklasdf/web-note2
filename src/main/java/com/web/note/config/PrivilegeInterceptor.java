package com.web.note.config;

import com.web.note.exception.UnauthorizedAccessException;
import com.web.note.security.RequireRole;
import com.web.note.security.Role;
import com.web.note.util.SessionUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PrivilegeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            RequireRole annotation = handlerMethod.getMethodAnnotation(RequireRole.class);

            if (annotation != null) {
                Role role = annotation.value();
                if (!hasRequiredPrivilege(request.getSession(), role)) {
                    throw new UnauthorizedAccessException("你没有权限访问");
                }
            }
        }

        return true;
    }

    private boolean hasRequiredPrivilege(HttpSession session,Role role) {
        switch (role){
            case USER:
                SessionUtil.getUser(session);
                return true;
            case BUSINESS_ADMIN:
                SessionUtil.getBusinessAdmin(session);
                return true;
            case SYSTEM_ADMIN:
                SessionUtil.getSystemAdmin(session);
                return true;
            case NORMAL_USER:
                SessionUtil.getNormalUser(session);
                return true;
            default:
                return false;
        }
    }
}
