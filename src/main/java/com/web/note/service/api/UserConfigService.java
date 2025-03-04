package com.web.note.service.api;

import com.web.note.entity.User;
import com.web.note.exception.InvalidInputException;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface UserConfigService {

    String loadUserConfig(HttpSession session, String key);

    Map<String,String> loadUserConfigs(HttpSession session);

    void loadUserConfigToSession(HttpSession session);

    void saveUserConfig(HttpSession session,String configKey,String configValue) throws InvalidInputException;

    void initUserConfig(User user);
}
