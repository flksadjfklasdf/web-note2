package com.web.note.handler.response;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

public class ViewResponse {
    public static ModelAndView errorView(HttpStatus status,String message){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("error-page");
        modelAndView.addObject("errorCode",status.value());
        modelAndView.addObject("message",message);
        modelAndView.setStatus(status);
        return modelAndView;
    }
}
