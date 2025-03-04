package com.web.note.config;

import com.web.note.dao.AppConfigMapper;
import com.web.note.entity.AppConfig;
import com.web.note.entity.AppConfigExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class BeanConfig {

    @Autowired
    public AppConfigMapper appConfigMapper;

    @Bean(name = "sysConfig")
    @Lazy(value = false)
    public Map<String,String> getConfig(){

        AppConfigExample appConfigExample=new AppConfigExample();
        List<AppConfig> appConfigs = appConfigMapper.selectByExample(appConfigExample);

        Map<String,String> config=new HashMap<>();

        for (AppConfig appConfig : appConfigs) {
            config.put(appConfig.getConfigItem(),appConfig.getConfigValue());
        }
        return config;
    }
}
