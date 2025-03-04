package com.web.note.service;

import com.web.note.config.SystemConfig;
import com.web.note.dao.UserDao;
import com.web.note.dao.UserMapper;
import com.web.note.entity.User;
import com.web.note.entity.UserExample;
import com.web.note.exception.*;
import com.web.note.handler.request.SearchParam;
import com.web.note.handler.request.UserIdAndPassword;
import com.web.note.handler.request.UsernameAndPassword;
import com.web.note.handler.response.SearchUserEntity;
import com.web.note.handler.response.UserToShow;
import com.web.note.service.api.*;
import com.web.note.util.Security;
import com.web.note.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.web.note.constant.DatabaseConstant.*;
import static com.web.note.util.MathUtil.intDivideCeil;
import static com.web.note.util.Security.escapeSQLike;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    public UserMapper userMapper;
    @Autowired
    public SystemConfig systemConfig;
    @Autowired
    public ApplicationService applicationService;
    @Autowired
    public AsyncService asyncService;
    @Autowired
    public UserDao userDao;


    @Autowired
    public CollectionService collectionService;
    @Autowired
    public UserConfigService userConfigService;

    @Override
    public User getUserByLogin(String userName, String password) {
        log.info("用户登录:{}", userName);

        User user = userDao.getUserByLogin(userName, password);

        if (user == null) {
            return null;
        }

        Integer status = user.getStatus();
        if (status != USER_OK) {
            if (status == USER_DISABLE) {
                throw new LoginException("你的账号已经被禁用,请与管理员联系!");
            }
            if (status == USER_TO_PERMIT) {
                throw new LoginException("你的账号还未通过验证");
            }
            throw new LoginException("你的账号状态存在异常");
        }

        log.info("用户登录成功:{}", userName);

        return user;
    }

    @Override
    public void signIn(String userName, String password) throws SignInFailedException, InvalidInputException {
        log.info("用户注册:{}", userName);

        User user = userDao.createUser(userName, password, COMMON_USER);

        if (systemConfig.isPermitRegister()) {
            userDao.addUser(user);

            initUser(user);
        } else {
            applicationService.submitRegisterApplication(user);
            throw new SignInFailedException("该注册需要管理员同意,已提交申请");
        }
    }

    @Override
    public void addUser(UsernameAndPassword param) throws InvalidInputException {

        User user = userDao.addUser(param.getUsername(), param.getPassword(), COMMON_USER);

        initUser(user);
    }


    @Override
    public void setLoginInformation(User userByLogin, HttpServletRequest request) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserIdEqualTo(userByLogin.getUserId());

        User user = new User();

        String clientIp = request.getRemoteAddr();

        user.setLoginAttempts(0);
        user.setLastLoginIp(clientIp);
        user.setLastLoginAt(new Date());

        userMapper.updateByExampleSelective(user, userExample);
    }


    @Override
    public void initSystemAdmin(User admin, UsernameAndPassword param) {
        if (systemConfig.getInitStep() != SYSTEM_CONFIG_SYSTEM_INIT_SYSTEM) {
            throw new IllegalStateException();
        }

        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserIdEqualTo(admin.getUserId()).andUserTypeEqualTo(SYSTEM_ADMIN);


        User user = new User();
        user.setUsername(param.getUsername());
        user.setPasswordHash(Security.passwordHash(param.getPassword(), admin.getUserId()));


        userMapper.updateByExampleSelective(user, userExample);

        systemConfig.setInitStep(SYSTEM_CONFIG_SYSTEM_INIT_OK);
    }

    @Override
    public SearchUserEntity getUsersList(SearchParam param) {

        String searchUsername = StringUtil.noEmptyString(param.getUsername());
        String searchEmail = StringUtil.noEmptyString(param.getEmail());
        Integer searchStatus;

        switch (param.getStatus()) {
            case "all":
                searchStatus = null;
                break;
            case "enable":
                searchStatus = USER_OK;
                break;
            case "disable":
                searchStatus = USER_DISABLE;
                break;
            default:
                throw new InvalidArgumentException("无效的参数");
        }


        Map<String, Object> map = new HashMap<>();

        setOrderParam(map, param);
        map.put("username", escapeSQLike(searchUsername));
        map.put("email", escapeSQLike(searchEmail));
        map.put("status", searchStatus);

        Integer offset = (param.getPage() - 1) * 10;
        map.put("offset", offset);

        List<User> users = userMapper.selectUsers(map);
        Integer integer = userMapper.countUsers(map);


        List<UserToShow> userToShows = new ArrayList<>();

        for (User user : users) {
            UserToShow userToShow = new UserToShow();
            userToShow.userId = user.getUserId();
            userToShow.username = user.getUsername();
            userToShow.createdAt = user.getCreatedAt();
            userToShow.lastLoginAt = user.getLastLoginAt();
            userToShow.status = user.getStatus();

            userToShows.add(userToShow);
        }


        SearchUserEntity searchUserEntity = new SearchUserEntity();

        searchUserEntity.setUsers(userToShows);
        int pages = intDivideCeil(integer, 10);
        int maxPages = Math.max(1, pages);
        searchUserEntity.setTotalPages(maxPages);


        return searchUserEntity;
    }

    @Override
    public int enableUser(List<String> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return 0;
        }

        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserIdIn(userIds);

        User userToUpdate = new User();
        userToUpdate.setStatus(USER_OK); // USER_OK 表示用户状态为启用

        return userMapper.updateByExampleSelective(userToUpdate, userExample);
    }

    @Override
    public int disableUser(List<String> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return 0;
        }

        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserIdIn(userIds);

        User userToUpdate = new User();
        userToUpdate.setStatus(USER_DISABLE);

        return userMapper.updateByExampleSelective(userToUpdate, userExample);
    }

    @Override
    public int deleteUser(List<String> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return 0;
        }

        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserIdIn(userIds);

        asyncService.deleteUserData(userIds);

        return userMapper.deleteByExample(userExample);
    }


    @Override
    public int modifyUserPassword(UserIdAndPassword param) {

        User userToUpdate = new User();
        userToUpdate.setPasswordHash(Security.passwordHash(param.getPassword(), param.getUserId()));

        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserIdEqualTo(param.getUserId());

        return userMapper.updateByExampleSelective(userToUpdate, userExample);
    }


    @Override
    public String getUserNameById(String appUserId) {

        User user = userMapper.selectByPrimaryKey(appUserId);

        if (user == null) {
            throw new NoResultException("没有找到结果");
        }

        return user.getUsername();
    }


    @Override
    public void initUser(User user) {
        collectionService.createDefaultCollection(user);
        userConfigService.initUserConfig(user);
    }


    private void setOrderParam(Map<String, Object> map, SearchParam param) {

        String order = param.getSort();

        String orderField = order.substring(0, order.length() - 1);
        String orderMethod = order.substring(order.length() - 1);

        switch (orderField) {
            case "username":
                map.put("order", "username");
                break;
            case "createdAt":
                map.put("order", "created_at");
                break;
            case "lastLoginAt":
                map.put("order", "last_login_at");
                break;
            default:
                throw new InvalidArgumentException();
        }

        switch (orderMethod) {
            case "0":
                map.put("direction", "asc");
                break;
            case "1":
                map.put("direction", "desc");
                break;
            default:
                throw new InvalidArgumentException();
        }
    }

}
