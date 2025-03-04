package com.web.note.service;

import com.web.note.config.SystemConfig;
import com.web.note.dao.UserMapper;
import com.web.note.entity.User;
import com.web.note.service.api.SystemService;
import com.web.note.util.Security;
import com.web.note.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.web.note.constant.DatabaseConstant.*;

@Service
@Slf4j
public class SystemServiceImpl implements SystemService {

    @Autowired
    public UserMapper userMapper;
    @Autowired
    public SystemConfig systemConfig;

    @Override
    public void initSystem() {

        log.error("由于没有系统管理员,将自动创建系统管理员");

        User admin=new User();

        admin.setUserType(SYSTEM_ADMIN);
        admin.setCreatedAt(new Date());
        admin.setStatus(USER_OK);
        String uid = Security.getUid();
        admin.setUserId(uid);
        admin.setUsername(DEFAULT_SYSTEM_ADMIN_NAME);

        String userPassword= StringUtil.randomString(16);
        log.error("创建的系统管理员名称为{}",DEFAULT_SYSTEM_ADMIN_NAME);
        log.error("创建的系统管理员密码为{}",userPassword);
        String encryptPassword=Security.passwordHash(userPassword,uid);

        admin.setPasswordHash(encryptPassword);

        userMapper.insert(admin);

        log.error("管理员已经初始化完毕,请使用管理员进行登录!");

        systemConfig.setInitStep(SYSTEM_CONFIG_SYSTEM_INIT_SYSTEM);
    }
}
