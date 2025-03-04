package com.web.note.handler.controller;

import com.web.note.business.UserBusiness;
import com.web.note.config.SystemConfig;
import com.web.note.dao.UserDao;
import com.web.note.entity.User;
import com.web.note.exception.InvalidInputException;
import com.web.note.exception.SignInFailedException;
import com.web.note.exception.UserStatusException;
import com.web.note.handler.request.SearchParam;
import com.web.note.handler.request.UserIdAndPassword;
import com.web.note.handler.request.UserPassword;
import com.web.note.handler.request.UsernameAndPassword;
import com.web.note.handler.response.*;
import com.web.note.manager.SessionManager;
import com.web.note.security.RequireRole;
import com.web.note.security.Role;
import com.web.note.service.CaptchaService;
import com.web.note.service.IpBanService;
import com.web.note.service.api.FileService;
import com.web.note.service.api.UserConfigService;
import com.web.note.service.api.UserService;
import com.web.note.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Objects;

import static com.web.note.constant.DatabaseConstant.*;
import static com.web.note.constant.PropertiesName.*;
import static com.web.note.constant.StatusConstant.*;
import static com.web.note.constant.ViewConstant.VIEW_LOGIN;

@Slf4j
@Validated
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    public UserService userService;
    @Autowired
    public SystemConfig systemConfig;
    @Autowired
    public FileService fileService;
    @Autowired
    public UserConfigService userConfigService;
    @Autowired
    public UserBusiness userBusiness;
    @Autowired
    public SessionManager sessionManager;
    @Autowired
    public IpBanService ipBanService;
    @Autowired
    public CaptchaService captchaService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(HttpServletRequest request,
                        @RequestParam("userName") String userName,
                        @RequestParam("password") String userPassword,
                        @RequestParam(name = "keep-login", required = false, defaultValue = "") String keepLogin,
                        @RequestParam(name = "captcha-text", required = false, defaultValue = "") String captchaText,
                        HttpSession session ,
                        HttpServletRequest httpServletRequest,
                        HttpServletResponse httpServletResponse) {

        User userByLogin;
        try {
            userByLogin = userService.getUserByLogin(userName, userPassword);
        } catch (Exception e) {
            session.setAttribute(MESSAGE_NAME, e.getMessage());
            throw e;
        }
        boolean ipBanned = ipBanService.isIpBanned(request.getRemoteAddr());
        if(ipBanned){
            if(!captchaService.validateCaptcha(httpServletRequest,captchaText)){
                session.setAttribute(ENABLE_CAPTCHA,true);
                session.setAttribute(MESSAGE_NAME, "请输入正确的验证码！");
                return VIEW_LOGIN;
            }
        }

        if (userByLogin == null) {
            session.setAttribute(MESSAGE_NAME, "用户名或者密码错误！");
            session.setAttribute(ENABLE_CAPTCHA,true);
            ipBanService.banIp(request.getRemoteAddr(),1440,1);

            return VIEW_LOGIN;
        }

        if(ipBanned){
            ipBanService.unbanIp(request.getRemoteAddr(), 1);
            session.setAttribute(ENABLE_CAPTCHA,false);
        }

        userService.setLoginInformation(userByLogin, request);

        session.setAttribute(USER_NAME, userByLogin);


        if(Objects.equals("keep-login",keepLogin)) {
            sessionManager.createCookie(httpServletRequest,httpServletResponse,session);
        }

        userConfigService.loadUserConfigToSession(session);

        if (userByLogin.getUserType() == SYSTEM_ADMIN) {
            if (systemConfig.getInitStep() == SYSTEM_CONFIG_SYSTEM_INIT_OK) {
                return "redirect:/systemManage.html";
            }
            if (systemConfig.getInitStep() == SYSTEM_CONFIG_SYSTEM_INIT_SYSTEM) {
                return "redirect:/systemInit.html";
            }
        }
        if (userByLogin.getUserType() == BUSINESS_ADMIN) {
            return "redirect:/businessManage.html";
        }

        if (session.getAttribute(REDIRECT_VIEW) != null) {
            String view = (String) session.getAttribute(REDIRECT_VIEW);
            session.removeAttribute(REDIRECT_VIEW);

            return view;
        }

        return "redirect:/index.html";

    }

    @RequestMapping("/logout")
    public String logout(HttpSession session,HttpServletRequest request,HttpServletResponse response) {

        session.invalidate();
        sessionManager.clearCookie1(request,response);

        return "redirect:/index.html";
    }

    @ResponseBody
    @PostMapping("/signin")
    public ResultEntity<Object> signin(HttpSession session, @RequestBody UsernameAndPassword user) {

        ResultEntity<Object> result = new ResultEntity<>();

        if (!systemConfig.initOK()) {
            result.setMessage("系统尚未进行初始化,请先对系统初始化!请使用管理员账号进行初始化");
            result.setStatus(NO_INIT);
            return result;
        }
        if (systemConfig.isDenyRegister()) {
            result.setMessage("系统配置不允许任何人进行注册!");
            result.setStatus(REQUEST_REFUSED);
            return result;
        }

        try {
            userService.signIn(user.getUsername(), user.getPassword());
        } catch (SignInFailedException e) {
            result.setMessage("注册失败:" + e.getMessage());
            result.setStatus(SERVICE_ERROR);
            return result;
        } catch (InvalidInputException e) {
            result.setMessage("注册失败:" + e.getMessage());
            result.setStatus(INVALID_INPUT);
            return result;
        }
        session.setAttribute(MESSAGE_NAME, "注册成功!");
        return ResultEntity.resultOK();
    }

    @ResponseBody
    @RequireRole(Role.SYSTEM_ADMIN)
    @RequestMapping("/initSystemAdmin")
    public ResultEntity<Object> initSystemAdmin(HttpSession session, @RequestBody @Validated UsernameAndPassword param) {

        ResultEntity<Object> result = new ResultEntity<>();

        try {
            User admin = SessionUtil.getSystemAdmin(session);

            userService.initSystemAdmin(admin, param);

            session.removeAttribute(USER_NAME);

            session.setAttribute(MESSAGE_NAME, "你已经更改了账号和密码,请重新登录!");

            result.setStatus(OK);
            return result;
        } catch (Exception e) {
            result.setStatus(SERVICE_ERROR);
            return result;
        }
    }

    @ResponseBody
    @RequireRole(Role.BUSINESS_ADMIN)
    @RequestMapping("/search")
    public ResultEntity<SearchUserEntity> getUser(@RequestBody @Validated SearchParam param) {

        SearchUserEntity entity = userService.getUsersList(param);

        ResultEntity<SearchUserEntity> result = new ResultEntity<>();
        result.setStatus(OK);
        result.setData(entity);

        return result;
    }

    @ResponseBody
    @RequireRole(Role.BUSINESS_ADMIN)
    @RequestMapping("/add")
    public ResultEntity<Object> addUser(@RequestBody @Validated UsernameAndPassword param) throws InvalidInputException {

        userService.addUser(param);

        return ResultEntity.resultOK();
    }

    @ResponseBody
    @RequireRole(Role.BUSINESS_ADMIN)
    @RequestMapping("/modify")
    public ResultEntity<Object> modifyUser(@RequestBody @Validated UserIdAndPassword param) {

        userService.modifyUserPassword(param);

        return ResultEntity.resultOK();
    }

    @ResponseBody
    @RequireRole(Role.USER)
    @RequestMapping("/modifyMyPassword")
    public ResultEntity<Object> modifyMyPassword(HttpSession session, @RequestBody @Validated UserPassword password) {

        User user = SessionUtil.getUser(session);

        UserIdAndPassword param = new UserIdAndPassword();
        param.setUserId(user.getUserId());
        param.setPassword(password.getPassword());

        userService.modifyUserPassword(param);

        return ResultEntity.resultOK();
    }

    @ResponseBody
    @RequireRole(Role.BUSINESS_ADMIN)
    @RequestMapping("/delete")
    public ResultEntity<Object> deleteUser(@RequestBody @NotEmpty List<String> userIds) {

        userService.deleteUser(userIds);

        return ResultEntity.resultOK();
    }

    @ResponseBody
    @RequireRole(Role.BUSINESS_ADMIN)
    @RequestMapping("/disable")
    public ResultEntity<Object> disableUser(@RequestBody @NotEmpty List<String> userIds) {

        userService.disableUser(userIds);

        return ResultEntity.resultOK();
    }

    @ResponseBody
    @RequireRole(Role.BUSINESS_ADMIN)
    @RequestMapping("/enable")
    public ResultEntity<Object> enableUser(@RequestBody @NotEmpty List<String> userIds) {

        userService.enableUser(userIds);

        return ResultEntity.resultOK();
    }

    @ResponseBody
    @RequireRole(Role.USER)
    @RequestMapping("/getSecurityInfo")
    public ResultEntity<UserSecurityInfo> getSecurityInfo(HttpServletRequest request, HttpSession httpSession) {
        User user = SessionUtil.getUser(httpSession);

        ResultEntity<UserSecurityInfo> result = new ResultEntity<>();

        UserSecurityInfo securityInfo = new UserSecurityInfo();
        securityInfo.setUsername(user.getUsername());
        securityInfo.setEmail(user.getEmail());
        securityInfo.setIp(request.getRemoteAddr());
        securityInfo.setLastIp(user.getLastLoginIp());
        securityInfo.setLastLogin(user.getLastLoginAt());

        result.setData(securityInfo);
        result.setStatus(OK);

        return result;
    }

    @ResponseBody
    @RequireRole(Role.USER)
    @RequestMapping("/getUserBasicInfo")
    public ResultEntity<UserBasicInfo> getUserBasicInfo(HttpSession session) {
        User user = SessionUtil.getUser(session);

        ResultEntity<UserBasicInfo> result = new ResultEntity<>();

        UserBasicInfo userBasicInfo = new UserBasicInfo();

        userBasicInfo.setUsername(user.getUsername());

        String userType;

        switch (user.getUserType()) {
            case COMMON_USER:
                userType = "普通用户";
                break;
            case BUSINESS_ADMIN:
                userType = "业务管理员";
                break;
            case SYSTEM_ADMIN:
                userType = "系统管理员";
                break;
            default:
                userType = null;
        }

        userBasicInfo.setUserRole(userType);
        userBasicInfo.setRegistrationDate(user.getCreatedAt());

        ResourceInfo resourceInfo = fileService.getResourceInfo(user);

        userBasicInfo.setTotalAvailableSpace(resourceInfo.getSpaceLimit());
        userBasicInfo.setArticleCount(resourceInfo.getTotalArticles());
        userBasicInfo.setTotalFileSize(resourceInfo.getUsedSpace());
        userBasicInfo.setCollectionCount(resourceInfo.getTotalCollections());

        result.setData(userBasicInfo);

        return result;
    }
    @ResponseBody
    @RequireRole(Role.USER)
    @RequestMapping("/getRemainingSpace")
    public ResultEntity<RemainingSpace> getRemainingSpace(HttpSession session) {
        User user = SessionUtil.getUser(session);
        int availableSpace = userBusiness.getAvailableSpace(user);
        RemainingSpace data = new RemainingSpace(availableSpace);
        return ResultEntity.resultOKWithData(data);
    }

}
