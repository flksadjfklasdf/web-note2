package com.web.note.service;

import com.web.note.config.SystemConfig;
import com.web.note.handler.request.AppConfig;
import com.web.note.handler.request.UserConfig;
import com.web.note.service.api.ConfigService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

import static com.web.note.constant.DatabaseConstant.CONFIG_REGISTER_MODE;
import static com.web.note.constant.DatabaseConstant.CONFIG_USER_SPACE;

@Service
@Slf4j
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    public SystemConfig config;

    @Override
    public AppConfig getBusinessConfig(HttpSession session) {

        AppConfig appConfig=new AppConfig();
        UserConfig user = new UserConfig();
        appConfig.setUser(user);

        CapacityResult capacityResult = convertCapacity(config.getConfig(CONFIG_USER_SPACE));

        user.setRegister(Integer.parseInt(config.getConfig(CONFIG_REGISTER_MODE)));
        user.setUnit(capacityResult.getUnit());
        user.setSpace(capacityResult.getValue());

        return appConfig;
    }

    @Override
    public void updateConfig(HttpSession session, AppConfig appConfig) {
        if (appConfig != null && appConfig.getUser() != null) {
            UserConfig userConfig = appConfig.getUser();

            config.setConfig(CONFIG_REGISTER_MODE, String.valueOf(userConfig.getRegister()));

            int space = userConfig.getSpace();
            String unit = userConfig.getUnit();

            config.setConfig(CONFIG_USER_SPACE, space+unit);
        } else {
            throw new IllegalArgumentException("AppConfig or UserConfig is null.");
        }
    }

    public static CapacityResult convertCapacity(String size) {
        int value=Integer.parseInt(size.substring(0,size.length()-2));
        String unit=size.substring(size.length()-2);
        return new CapacityResult(value,unit);
    }

    @Data
    @AllArgsConstructor
    public static class CapacityResult {
        private final int value;
        private final String unit;
    }
}
