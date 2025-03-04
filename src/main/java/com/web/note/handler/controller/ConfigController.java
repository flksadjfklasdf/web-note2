package com.web.note.handler.controller;

import com.web.note.handler.request.AppConfig;
import com.web.note.handler.response.ResultEntity;
import com.web.note.security.RequireRole;
import com.web.note.security.Role;
import com.web.note.service.api.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.web.note.constant.StatusConstant.OK;


@Slf4j
@Validated
@RestController
@RequestMapping("/config")
public class ConfigController {


    @Autowired
    public ConfigService configService;

    @RequireRole(Role.BUSINESS_ADMIN)
    @RequestMapping("/getBConfig")
    public ResultEntity<AppConfig> getConfig(HttpSession session) {

        ResultEntity<AppConfig> entity = new ResultEntity<>();

        AppConfig config = configService.getBusinessConfig(session);

        entity.setData(config);
        entity.setStatus(OK);

        return entity;
    }

    @RequireRole(Role.BUSINESS_ADMIN)
    @RequestMapping("/updateBConfig")
    public ResultEntity<Object> updateConfig(HttpSession session,@RequestBody @Validated  AppConfig config) {

        configService.updateConfig(session, config);

        return ResultEntity.resultOK();
    }

}
