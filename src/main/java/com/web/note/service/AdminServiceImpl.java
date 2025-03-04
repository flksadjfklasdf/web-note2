package com.web.note.service;

import com.web.note.dao.UserDao;
import com.web.note.dao.UserMapper;
import com.web.note.entity.User;
import com.web.note.entity.UserExample;
import com.web.note.exception.InvalidInputException;
import com.web.note.exception.SystemFatalException;
import com.web.note.exception.ValidateException;
import com.web.note.handler.request.UserId;
import com.web.note.handler.request.UserIdAndCredence;
import com.web.note.handler.request.UsernameAndPassword;
import com.web.note.handler.response.UserToReturn;
import com.web.note.service.api.AdminService;
import com.web.note.util.Security;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.web.note.constant.DatabaseConstant.*;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    public UserMapper userMapper;
    @Autowired
    public UserDao userDao;

    @Override
    public void addAdmin(UsernameAndPassword user) throws InvalidInputException {

        userDao.addUser(user.getUsername(), user.getPassword(), BUSINESS_ADMIN);

    }

    @Override
    public void disableAdmin(UserId user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserIdEqualTo(user.getUserId()).andUserTypeEqualTo(BUSINESS_ADMIN);

        List<User> users = userMapper.selectByExample(userExample);

        if (users == null || users.size() == 0) {
            throw new RuntimeException("没有找到该用户");
        }
        User user1 = users.get(0);

        User toModify = new User();

        if (user1.getStatus() == USER_OK) {
            toModify.setStatus(USER_DISABLE);
        } else {
            toModify.setStatus(USER_OK);
        }
        if (userMapper.updateByExampleSelective(toModify, userExample) != 1) {
            log.error("更改管理员失败!");
            throw new RuntimeException("更改管理员失败!");
        }
    }

    @Override
    public void deleteAdmin(UserId user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserIdEqualTo(user.getUserId()).andUserTypeEqualTo(BUSINESS_ADMIN);

        if (userMapper.deleteByExample(userExample) != 1) {
            log.error("删除管理员失败!");
            throw new RuntimeException("删除管理员失败!");
        }
    }

    @Override
    public void modifyAdmin(UserIdAndCredence user) {

        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserIdEqualTo(user.getUserId()).andUserTypeNotEqualTo(COMMON_USER);

        List<User> users = userMapper.selectByExample(userExample);
        if (users == null || users.size() != 1) {
            throw new SystemFatalException("系统严重错误:系统管理员状态错误");
        }
        User user1 = users.get(0);

        User adminToModify = new User();

        adminToModify.setPasswordHash(Security.passwordHash(user.getPassword(),user1.getUserId()));

        if (user1.getUserType() == SYSTEM_ADMIN) {
            if(user.getUsername().length()<10 || user.getPassword().length()<10){
                throw new ValidateException();
            }
            adminToModify.setUsername(user.getUsername());
        }
        userMapper.updateByExampleSelective(adminToModify, userExample);
    }

    @Override
    public List<UserToReturn> getBusinessAdmin() {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserTypeEqualTo(BUSINESS_ADMIN);

        List<User> users = userMapper.selectByExample(userExample);

        List<UserToReturn> toReturn = new ArrayList<>();

        for (User user : users) {
            UserToReturn u = new UserToReturn();

            u.setStatus(user.getStatus());
            u.setUserId(user.getUserId());
            u.setUsername(user.getUsername());
            u.setCreatedAt(user.getCreatedAt());
            u.setUserType(user.getUserType());

            toReturn.add(u);
        }

        return toReturn;
    }

    @Override
    public UserToReturn getSystemAdmin() {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserTypeEqualTo(SYSTEM_ADMIN);

        List<User> users = userMapper.selectByExample(userExample);

        if (users == null || users.size() != 1) {
            throw new SystemFatalException("系统严重错误:系统管理员状态错误");
        }
        User user = users.get(0);

        UserToReturn adminToReturn = new UserToReturn();

        adminToReturn.setStatus(user.getStatus());
        adminToReturn.setUserId(user.getUserId());
        adminToReturn.setUsername(user.getUsername());
        adminToReturn.setCreatedAt(user.getCreatedAt());
        adminToReturn.setUserType(user.getUserType());


        return adminToReturn;
    }
}
