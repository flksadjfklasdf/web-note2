package com.web.note.config;

import com.web.note.dao.UserMapper;
import com.web.note.entity.User;
import com.web.note.entity.UserExample;
import com.web.note.service.api.SystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;

import java.util.List;

import static com.web.note.constant.DatabaseConstant.*;
import static com.web.note.constant.PropertiesName.*;
import static com.web.note.util.StringUtil.empty;

@Slf4j
@Component
public class MyServletContextInitializer implements ServletContextInitializer {

    @Value("${app.res.url-prefix}")
    public String resPrefix;
    @Value("${app.img.url-prefix}")
    public String imgPrefix;

    @Value("${app.standard.url-prefix}")
    private String standardUrl;


    @Autowired
    public SystemConfig systemConfig;
    @Autowired
    public UserMapper userMapper;
    @Autowired
    public SystemService systemService;

    @Override
    public void onStartup(ServletContext servletContext) {
        if (resPrefix == null || resPrefix.equals("")) {
            servletContext.setAttribute(RES_URL_PREFIX, "");
        } else {
            servletContext.setAttribute(RES_URL_PREFIX, resPrefix);
        }

        if (imgPrefix == null || imgPrefix.equals("")) {
            servletContext.setAttribute(IMG_URL_PREFIX, "");
        } else {
            servletContext.setAttribute(IMG_URL_PREFIX, imgPrefix);
        }

        if (empty(standardUrl)){
            log.error("应用访问url未配置!");
            throw new RuntimeException("应用访问url未配置!");
        }else{
            servletContext.setAttribute(STANDARD_URL_PREFIX, standardUrl);
        }

        if (systemConfig.getInitStep() == SYSTEM_CONFIG_SYSTEM_INIT_ADMIN && !hasSystemAdmin()) {
            systemService.initSystem();
        }


    }


    public boolean hasSystemAdmin() {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserTypeEqualTo(SYSTEM_ADMIN);

        List<User> users = userMapper.selectByExample(userExample);

        if (users == null || users.size() == 0) {
            return false;
        }
        if(users.size()>1){
            throw new RuntimeException("存在多个系统管理员!");
        }
        return true;
    }
}
