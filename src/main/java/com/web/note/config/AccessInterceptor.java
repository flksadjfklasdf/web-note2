package com.web.note.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Slf4j
@Configuration
public class AccessInterceptor implements HandlerInterceptor {


    @Value("${app.standard.url-prefix}")
    private String standardUrl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestUrl = getRequestBaseUrl(request);

        log.debug("用户访问的url:{}", requestUrl);

        if (!Objects.equals(standardUrl, requestUrl)) {

            String correctedUrl = standardUrl + request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");

            response.sendRedirect(correctedUrl);
            log.debug("用户访问被完全重定向到: {}", correctedUrl);
            return false;
        }
        return true;
    }

    private String getRequestBaseUrl(HttpServletRequest request) {
        String protocol = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();

        if (("http".equals(protocol) && serverPort == 80) || ("https".equals(protocol) && serverPort == 443)) {
            return protocol + "://" + serverName;
        }

        return protocol + "://" + serverName + ":" + serverPort;
    }
}
