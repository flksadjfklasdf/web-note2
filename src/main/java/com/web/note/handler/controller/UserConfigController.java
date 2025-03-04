package com.web.note.handler.controller;

import com.web.note.constant.DatabaseConstant;
import com.web.note.exception.InvalidInputException;
import com.web.note.handler.response.CustomerInfo;
import com.web.note.handler.response.ResultEntity;
import com.web.note.security.RequireRole;
import com.web.note.security.Role;
import com.web.note.service.api.UserConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Arrays;

import static com.web.note.constant.DatabaseConstant.CODE_STYLE;
import static com.web.note.constant.DatabaseConstant.KEEP_LOGIN_MAX_DAYS;

@Slf4j
@Validated
@RequestMapping("/userConfig")
@RestController
public class UserConfigController {

    @Autowired
    public UserConfigService userConfigService;


    @RequireRole(Role.USER)
    @PostMapping("/getConfig")
    public ResultEntity<CustomerInfo> loadUserConfig(HttpSession session) {
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setConfigInfo(userConfigService.loadUserConfigs(session));
        return ResultEntity.resultOKWithData(customerInfo);
    }
    @RequireRole(Role.USER)
    @PostMapping("/saveCodeStyleConfig")
    public ResultEntity<Object> saveCodeStyleConfig(HttpSession session,@RequestParam("configValue") String configValue) throws InvalidInputException {

        if (!Arrays.asList(DatabaseConstant.CODE_STYLE_LIST).contains(configValue)) {
            throw new InvalidInputException("非法的代码风格");
        }

        userConfigService.saveUserConfig(session, CODE_STYLE, configValue);

        session.setAttribute(CODE_STYLE,configValue);

        return ResultEntity.resultOK();
    }
    @RequireRole(Role.USER)
    @PostMapping("/saveMaxKeepDays")
    public ResultEntity<Object> saveMaxKeepDays(HttpSession session,@RequestParam("keepLoginMaxDays")String keepLoginMaxDays) throws InvalidInputException{
        int maxDays;
        try{
            maxDays =Integer.parseInt(keepLoginMaxDays);
        }catch (Exception e){
            throw new InvalidInputException("无效的输入:"+keepLoginMaxDays);
        }
        if(maxDays<1||maxDays>180){
            throw new InvalidInputException("无效的输入:天数必须在1~180之内");
        }
        userConfigService.saveUserConfig(session, KEEP_LOGIN_MAX_DAYS, String.valueOf(maxDays));
        return ResultEntity.resultOK();
    }

}
