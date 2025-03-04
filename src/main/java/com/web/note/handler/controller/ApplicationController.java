package com.web.note.handler.controller;

import com.web.note.entity.User;
import com.web.note.exception.InvalidArgumentException;
import com.web.note.handler.request.AppSearchParam;
import com.web.note.handler.response.ResultEntity;
import com.web.note.handler.response.SearchApplicationEntity;
import com.web.note.security.RequireRole;
import com.web.note.security.Role;
import com.web.note.service.api.ApplicationService;
import com.web.note.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static com.web.note.constant.StatusConstant.OK;


@Slf4j
@Validated
@RestController
@RequestMapping("/application")
public class ApplicationController {

    @Autowired
    public ApplicationService applicationService;

    @RequireRole(Role.BUSINESS_ADMIN)
    @RequestMapping("/getApplication")
    public ResultEntity<SearchApplicationEntity> getApplication(@RequestBody @Validated AppSearchParam appSearchParam) {


        SearchApplicationEntity result = applicationService.search(appSearchParam);

        ResultEntity<SearchApplicationEntity> result1 = new ResultEntity<>();
        result1.setStatus(OK);
        result1.setData(result);

        return result1;
    }

    @RequireRole(Role.BUSINESS_ADMIN)
    @RequestMapping("/deny")
    public ResultEntity<Object> deny(HttpSession session, @NotEmpty @RequestBody List<String> appIds) {

        User admin = SessionUtil.getBusinessAdmin(session);

        applicationService.denyAll(admin, appIds);

        return ResultEntity.resultOK();
    }

    @RequireRole(Role.BUSINESS_ADMIN)
    @RequestMapping("/permit")
    public ResultEntity<Object> permit(HttpSession session, @NotEmpty @RequestBody List<String> appIds) {

        User admin = SessionUtil.getBusinessAdmin(session);

        applicationService.permitAll(admin, appIds);

        return ResultEntity.resultOK();
    }
}
