package com.web.note.config;

import com.web.note.dao.AppConfigMapper;
import com.web.note.entity.AppConfig;
import com.web.note.entity.AppConfigExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

import static com.web.note.constant.DatabaseConstant.*;

@Slf4j
@Component
public class SystemConfig {

    @Autowired
    private AppConfigMapper appConfigMapper;

    @Autowired
    @Qualifier("sysConfig")
    private Map<String, String> map;

    public String getConfig(String key) {
        return map.get(key);
    }

    public void setConfig(String key, String value) {

        updateDatabase(key, value);

        map.put(key, value);
    }

    private void updateDatabase(String key, String value) {
        AppConfigExample appConfigExample = new AppConfigExample();
        appConfigExample.createCriteria().andConfigItemEqualTo(key);


        AppConfig appConfig = new AppConfig();
        appConfig.setConfigValue(value);
        appConfigMapper.updateByExampleSelective(appConfig, appConfigExample);
    }


    public int getInitStep() {
        try {
            String s = map.get(SYSTEM_CONFIG_SYSTEM_INIT);
            return Integer.parseInt(s);
        } catch (Exception e) {
            log.error("严重系统错误,系统尚未初始化!!!", e);
            return -1;
        }
    }

    public void setInitStep(int step) {
        map.put(SYSTEM_CONFIG_SYSTEM_INIT, String.valueOf(step));

        updateDatabase(SYSTEM_CONFIG_SYSTEM_INIT, String.valueOf(step));
    }

    public int getStorageSpace() {
        String s = map.get(CONFIG_USER_SPACE);

        int value = Integer.parseInt(s.substring(0, s.length() - 2));
        String unit = s.substring(s.length() - 2);
        if (!Objects.equals(unit, "MB") && !Objects.equals(unit, "GB")) {
            log.error("数据库储存空间格式不匹配");
            throw new RuntimeException("数据库严重错误！");
        }
        return Objects.equals(unit, "MB") ? value * 1024 : value * 1024 * 1024;
    }

    public boolean initOK() {
        return getInitStep() == SYSTEM_CONFIG_SYSTEM_INIT_OK;
    }

    public int getRegisterMode() {
        return Integer.parseInt(map.get(CONFIG_REGISTER_MODE));
    }

    public boolean isDenyRegister() {
        return getRegisterMode() == CONFIG_REGISTER_VALUE_DENY;
    }

    public boolean isPermitRegister() {
        return getRegisterMode() == CONFIG_REGISTER_VALUE_PERMIT;
    }


}
