package com.web.note.handler.controller;

import com.web.note.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
public class CaptchaController {

    @Autowired
    CaptchaService captchaService;

    @GetMapping("/captcha/generate")
    @ResponseBody
    public ResponseEntity<byte[]> requestCaptcha(HttpServletRequest httpServletRequest) {
        BufferedImage bufferedImage = captchaService.generateCaptcha(httpServletRequest);

        // 将 BufferedImage 转换为字节数组
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "png", baos);
            baos.flush();
            byte[] imageBytes = baos.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);  // 设置响应内容类型
            headers.setCacheControl("no-cache, no-store, must-revalidate");

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





}
