package com.web.note.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AccessInterceptor accessInterceptor;
    @Autowired
    private SessionInterceptor sessionInterceptor;


//    @Value("${app.restrict.domain}")
//    private boolean restrictDomain;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns("/**");

        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/collection/**")
                .addPathPatterns("/document/**")
                .addPathPatterns("/file/**")
                .excludePathPatterns("/document/getDocumentById")
                .excludePathPatterns("/file/d/**")
                .excludePathPatterns("/file/authorize");

        registry.addInterceptor(new PrivilegeInterceptor());

//        if (restrictDomain) {
//            registry.addInterceptor(accessInterceptor)
//                    .addPathPatterns("/**")
//                    .excludePathPatterns("/static/**");
//        }
    }
}