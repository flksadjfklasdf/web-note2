package com.web.note.util;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {
    public static boolean isAjaxRequest(HttpServletRequest request){
        String accept = request.getHeader("Accept");

        String header = request.getHeader("X-Requested-With");

        return (accept != null && accept.contains("application/json")) || (header != null && header.contains("XMLHttpRequest"));

    }
}
