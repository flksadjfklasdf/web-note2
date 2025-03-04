package com.web.note.service;

import com.google.gson.Gson;
import com.web.note.dao.ApplicationMapper;
import com.web.note.dao.UserDao;
import com.web.note.entity.Application;
import com.web.note.entity.ApplicationExample;
import com.web.note.entity.User;
import com.web.note.exception.NoResultException;
import com.web.note.handler.request.AppSearchParam;
import com.web.note.handler.response.ApplicationToReturn;
import com.web.note.handler.response.SearchApplicationEntity;
import com.web.note.service.api.ApplicationService;
import com.web.note.service.api.UserService;
import com.web.note.util.Security;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.web.note.constant.DatabaseConstant.*;
import static com.web.note.util.MathUtil.intDivideCeil;

@Service
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    public ApplicationMapper applicationMapper;
    @Autowired
    public UserService userService;
    @Autowired
    public UserDao userDao;

    @Override
    public void submitRegisterApplication(User user) {
        Application application = new Application();

        application.setAppId(Security.getUid());
        application.setAppType(REGISTER_APPLICATION);
        application.setAppSubmitDate(new Date());
        application.setAppStatus(APP_NOT_HANDLE);
        application.setAppUserId(user.getUserId());
        application.setAppContent(user.getUsername());

        Gson gson = new Gson();
        String json = gson.toJson(user);

        application.setAppData(json);

        applicationMapper.insert(application);
    }

    @Override
    public SearchApplicationEntity search(AppSearchParam appSearchParam) {
        Integer status;
        switch (appSearchParam.status) {
            case "deny":
                status = APP_DENY;
                break;
            case "permit":
                status = APP_PERMIT;
                break;
            case "noHandle":
                status = APP_NOT_HANDLE;
                break;
            case "failed":
                status = APP_FAILED;
                break;
            default:
                status = null;
        }
        Integer page = appSearchParam.page;
        Map<String, Integer> map = new HashMap<>();

        map.put("status", status);
        Integer offset = (page - 1) * 10;
        map.put("offset", offset);

        List<Application> applications = applicationMapper.searchApplicationsByParam(map);
        Integer count = applicationMapper.countApplications(map);

        List<ApplicationToReturn> returns = new ArrayList<>();


        for (Application application : applications) {
            ApplicationToReturn applicationToReturn = new ApplicationToReturn();

            applicationToReturn.setAppId(application.getAppId());
            applicationToReturn.setAppSubmitDate(application.getAppSubmitDate());
            applicationToReturn.setAppType(application.getAppType());
            applicationToReturn.setAppValue(application.getAppValue());
            applicationToReturn.setAppStatus(application.getAppStatus());

            String appUserId = application.getAppDealUserId();

            if (appUserId != null) {
                try {
                    applicationToReturn.setDealUser(application.getAppDealUserName());
                } catch (NoResultException e) {
                    applicationToReturn.setDealUser("未知");
                }
            }

            applicationToReturn.setDealDate(application.getAppDealDate());

            if (Objects.equals(application.getAppType(), REGISTER_APPLICATION)) {
                applicationToReturn.setUsername(application.getAppContent());
            } else {
                try {
                    applicationToReturn.setUsername(userService.getUserNameById(application.getAppUserId()));
                } catch (NoResultException e) {
                    applicationToReturn.setUsername("未知");
                }
            }
            returns.add(applicationToReturn);
        }

        SearchApplicationEntity searchApplicationEntity = new SearchApplicationEntity();
        searchApplicationEntity.setApps(returns);

        int pages = intDivideCeil(count, 10);
        int maxPages = Math.max(1, pages);
        searchApplicationEntity.setTotalPages(maxPages);


        return searchApplicationEntity;
    }

    @Override
    public void permitAll(User dealUser, List<String> appIds) {

        ApplicationExample applicationExample = new ApplicationExample();
        applicationExample.createCriteria().andAppStatusEqualTo(APP_NOT_HANDLE).andAppIdIn(appIds);


        Application application = new Application();
        application.setAppStatus(APP_PERMIT);
        application.setAppDealDate(new Date());
        application.setAppDealUserId(dealUser.getUserId());
        application.setAppDealUserName(dealUser.getUsername());

        applicationMapper.updateByExampleSelective(application, applicationExample);

        for (String appId : appIds) {
            handlePermit(appId);
        }

    }

    @Override
    public void denyAll(User dealUser, List<String> appIds) {
        ApplicationExample applicationExample = new ApplicationExample();
        applicationExample.createCriteria().andAppStatusEqualTo(APP_NOT_HANDLE).andAppIdIn(appIds);

        Application application = new Application();
        application.setAppStatus(APP_DENY);
        application.setAppDealDate(new Date());
        application.setAppDealUserId(dealUser.getUserId());
        application.setAppDealUserName(dealUser.getUsername());

        applicationMapper.updateByExampleSelective(application, applicationExample);
    }


    public void handlePermit(String appId) {
        Application application = applicationMapper.selectByPrimaryKey(appId);

        if (Objects.equals(application.getAppType(), REGISTER_APPLICATION)) {
            handleRegisterPermit(application);
        }
    }

    public void handleRegisterPermit(Application application) {

        Gson gson = new Gson();

        User user = gson.fromJson(application.getAppData(), User.class);

        try {
            userDao.addUser(user);

            userService.initUser(user);
        }catch (Exception e){
            application.setAppStatus(APP_FAILED);
        }
        applicationMapper.updateByPrimaryKeyWithBLOBs(application);
    }

}
