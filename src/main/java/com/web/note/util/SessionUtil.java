package com.web.note.util;

import com.web.note.entity.User;
import com.web.note.exception.UnLoginException;
import com.web.note.exception.UnauthorizedAccessException;

import javax.servlet.http.HttpSession;

import static com.web.note.constant.DatabaseConstant.*;
import static com.web.note.constant.PropertiesName.USER_NAME;

public class SessionUtil {
    public static User getUser(HttpSession session){

        User attribute = (User) session.getAttribute(USER_NAME);

        if (attribute != null) {
            return attribute;
        }
        throw new UnLoginException();
    }
    public static User getNormalUser(HttpSession session){

        User attribute = (User) session.getAttribute(USER_NAME);

        if (attribute != null) {
            if(attribute.getUserType()== COMMON_USER){
                return attribute;
            }else{
                throw new UnauthorizedAccessException("你无权访问此内容");
            }
        }

        throw new UnLoginException();
    }
    public static User getSystemAdmin(HttpSession session){

        User attribute = (User) session.getAttribute(USER_NAME);

        if (attribute != null) {
            if(attribute.getUserType()==SYSTEM_ADMIN){
                return attribute;
            }else{
                throw new UnauthorizedAccessException("你无权访问此内容");
            }
        }
        throw new UnLoginException();
    }
    public static User getBusinessAdmin(HttpSession session){

        User attribute = (User) session.getAttribute(USER_NAME);

        if (attribute != null) {
            if(attribute.getUserType()==BUSINESS_ADMIN){
                return attribute;
            }else{
                throw new UnauthorizedAccessException("你无权访问此内容");
            }
        }
        throw new UnLoginException();
    }
}
