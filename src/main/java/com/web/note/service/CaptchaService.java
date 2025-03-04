package com.web.note.service;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;

@Service
public class CaptchaService {

    @Autowired
    private DefaultKaptcha captchaProducer;

    public boolean validateCaptcha(HttpServletRequest request, String userCaptchaInput) {

        HttpSession session = request.getSession();

        String sessionCaptcha;

        synchronized (session) {
            sessionCaptcha = (String) session.getAttribute("captchaCode");
            session.removeAttribute("captchaCode");
        }

        return sessionCaptcha!= null && sessionCaptcha.equalsIgnoreCase(userCaptchaInput);
    }

    public BufferedImage generateCaptcha(HttpServletRequest request){
        HttpSession session = request.getSession();

        String captchaText = captchaProducer.createText();

        BufferedImage captchaImage = captchaProducer.createImage(captchaText);

        session.setAttribute("captchaCode", captchaText);
        return captchaImage;
    }
}
