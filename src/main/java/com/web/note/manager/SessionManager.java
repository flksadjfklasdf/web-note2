package com.web.note.manager;

import com.web.note.dao.UserMapper;
import com.web.note.dao.UserSessionMapper;
import com.web.note.entity.User;
import com.web.note.entity.UserSession;
import com.web.note.entity.UserSessionExample;
import com.web.note.service.api.UserConfigService;
import com.web.note.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.web.note.constant.DatabaseConstant.KEEP_LOGIN_MAX_DAYS;
import static com.web.note.constant.DatabaseConstant.COMMON_USER;
import static com.web.note.constant.PropertiesName.USER_NAME;
import static com.web.note.util.Security.generateSecureId;

@Slf4j
@Service
public class SessionManager {
    @Autowired
    private UserSessionMapper userSessionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserConfigService userConfigService;




    public void createSession(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse){
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null) {
            return;
        }

        HttpSession session= httpServletRequest.getSession();

        User u=(User) session.getAttribute(USER_NAME);
        if(u!=null){
            return;
        }

        Optional<Cookie> keepLoginMaxDaysCookie = Arrays.stream(cookies)
                .filter(cookie -> "TID".equals(cookie.getName()))
                .findFirst();
        Cookie cookie= keepLoginMaxDaysCookie.orElse(null);
        if (cookie == null) {
            return;
        }
        String sid = cookie.getValue();

        Date d=new Date();

        UserSessionExample userSessionExample=new UserSessionExample();
        userSessionExample.createCriteria().andExpiryDateGreaterThan(d).andSessionIdEqualTo(sid);


        List<UserSession> userSessions = userSessionMapper.selectByExample(userSessionExample);

        if (userSessions.isEmpty()) {
            clearCookie0(httpServletRequest,httpServletResponse);
            return;
        }
        UserSession us = userSessions.get(0);
        String userId = us.getUserId();

        User user = userMapper.selectByPrimaryKey(userId);

        if (user == null) {
            log.error("不存在的用户:{}",userId);
        }
        session.setAttribute(USER_NAME,user);
        userConfigService.loadUserConfigToSession(session);
    }

    private void clearCookie0(HttpServletRequest request,HttpServletResponse response) {
        String contextPath = request.getContextPath();
        String cookiePath = contextPath.isEmpty() ? "/" : contextPath;

        Cookie cookie = new Cookie("TID", "");
        cookie.setMaxAge(0);
        cookie.setPath(cookiePath);
        response.addCookie(cookie);
    }
    public void clearCookie1(HttpServletRequest request,HttpServletResponse response) {
        clearCookie0(request,response);


        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }

        Optional<Cookie> keepLoginMaxDaysCookie = Arrays.stream(cookies)
                .filter(cookie -> "TID".equals(cookie.getName()))
                .findFirst();

        Cookie cookie= keepLoginMaxDaysCookie.orElse(null);
        if (cookie == null) {
            return;
        }

        String sid = cookie.getValue();

        userSessionMapper.deleteByPrimaryKey(sid);
    }

    public void createCookie(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse, HttpSession session){

        User user = SessionUtil.getUser(session);
        if(user.getUserType() != COMMON_USER){
            return;
        }

        int days;
        try{
            days=Integer.parseInt(userConfigService.loadUserConfig(session,KEEP_LOGIN_MAX_DAYS));
        }catch (Exception e){
            e.printStackTrace();
            days=7;
        }

        int seconds=days*24*3600;

        String secureId = generateSecureId();

        String contextPath = httpServletRequest.getContextPath();
        String cookiePath = contextPath.isEmpty() ? "/" : contextPath;

        httpServletResponse.setHeader("Set-Cookie", "TID=" + secureId + "; Max-Age=" + seconds + "; Path=" + cookiePath + "; Secure; HttpOnly; SameSite=Strict");


        UserSession userSession=new UserSession();
        userSession.setSessionId(secureId);
        userSession.setCreateDate(new Date());

        long expiryTimeMillis = userSession.getCreateDate().getTime() + (long) days * 24L * 3600L * 1000L;
        userSession.setExpiryDate(new Date(expiryTimeMillis));
        userSession.setUserId(user.getUserId());

        userSessionMapper.insert(userSession);
    }


    @Scheduled(cron = "0 0 0 * * *") // 每天凌晨0点触发
    public void clearDatabase() {
        Date currentDate = new Date();
        UserSessionExample userSessionExample = new UserSessionExample();
        userSessionExample.createCriteria()
                .andExpiryDateLessThan(currentDate);
        userSessionMapper.deleteByExample(userSessionExample);
    }

}
