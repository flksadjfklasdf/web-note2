package com.web.note.service;

import com.web.note.dao.UserConfigMapper;
import com.web.note.entity.User;
import com.web.note.entity.UserConfig;
import com.web.note.entity.UserConfigExample;
import com.web.note.service.api.UserConfigService;
import com.web.note.util.Security;
import com.web.note.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

import static com.web.note.constant.DatabaseConstant.USER_CONFIG;

@Slf4j
@Service
public class UserConfigServiceImpl implements UserConfigService {

    @Autowired
    UserConfigMapper userConfigMapper;

    @Override
    public String loadUserConfig(HttpSession session, String key){
        Map<String, String> stringStringMap = this.loadUserConfigs(session);
        return stringStringMap.get(key);
    }
    @Override
    public Map<String, String> loadUserConfigs(HttpSession session) {
        User user = SessionUtil.getUser(session);

        String userId = user.getUserId();
        return this.loadUserConfigs(userId);
    }

    private Map<String, String> loadUserConfigs(String userId) {

        UserConfigExample userConfigExample=new UserConfigExample();
        userConfigExample.createCriteria().andUserIdEqualTo(userId);

        Map<String,String> map=new HashMap<>();

        List<UserConfig> userConfigs=userConfigMapper.selectByExample(userConfigExample);
        if(userConfigs!=null&&userConfigs.size()>0){
            for (UserConfig userConfig : userConfigs) {
                String key=userConfig.getConfigKey();
                String value=userConfig.getConfigValue();

                map.put(key,value);
            }
        }
        return map;
    }

    @Override
    public void loadUserConfigToSession(HttpSession session) {
        Map<String, String> stringStringMap = this.loadUserConfigs(session);
        String s = stringStringMap.get("codeStyle");

        if (s != null) {
            session.setAttribute("codeStyle", s);
        }

    }

    @Override
    public void saveUserConfig(HttpSession session, String configKey, String configValue){

        User user = SessionUtil.getUser(session);

        if (!hasConfigKey(user.getUserId(), configKey)) {

            String uid = Security.getUid();

            UserConfig userConfig = new UserConfig();
            userConfig.setConfigKey(configKey);
            userConfig.setConfigValue(configValue);
            userConfig.setUserId(user.getUserId());
            userConfig.setConfigId(uid);
            userConfig.setCreatedAt(new Date());
            userConfig.setUpdatedAt(new Date());

            userConfigMapper.insertSelective(userConfig);
        } else {
            UserConfig userConfig = new UserConfig();
            userConfig.setConfigValue(configValue);
            userConfig.setUpdatedAt(new Date());

            UserConfigExample userConfigExample=new UserConfigExample();
            userConfigExample.createCriteria().andUserIdEqualTo(user.getUserId()).andConfigKeyEqualTo(configKey);

            userConfigMapper.updateByExampleSelective(userConfig,userConfigExample);
        }
    }

    @Override
    public void initUserConfig(User user) {
        Set<String> strings = USER_CONFIG.keySet();
        for (String string : strings) {
            String value=USER_CONFIG.get(string);


            String uid = Security.getUid();
            UserConfig userConfig = new UserConfig();
            userConfig.setConfigKey(string);
            userConfig.setConfigValue(value);
            userConfig.setUserId(user.getUserId());
            userConfig.setConfigId(uid);
            userConfig.setCreatedAt(new Date());
            userConfig.setUpdatedAt(new Date());

            userConfigMapper.insertSelective(userConfig);
        }

    }

    private boolean hasConfigKey(String userId,String configKey){
        UserConfigExample userConfigExample=new UserConfigExample();
        userConfigExample.createCriteria().andUserIdEqualTo(userId).andConfigKeyEqualTo(configKey);
        return userConfigMapper.countByExample(userConfigExample) > 0;
    }
}
