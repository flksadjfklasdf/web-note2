package com.web.note.service.api;

import com.web.note.handler.request.AppConfig;

import javax.servlet.http.HttpSession;

public interface ConfigService {

    AppConfig getBusinessConfig(HttpSession session);

    void updateConfig(HttpSession session,AppConfig appConfig);
}
