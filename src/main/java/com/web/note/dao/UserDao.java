package com.web.note.dao;

import com.web.note.config.SystemConfig;
import com.web.note.entity.User;
import com.web.note.entity.UserExample;
import com.web.note.exception.InvalidInputException;
import com.web.note.exception.SignInFailedException;
import com.web.note.exception.SystemFatalException;
import com.web.note.util.Security;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static com.web.note.constant.DatabaseConstant.USER_OK;
import static com.web.note.util.Security.getUid;
import static com.web.note.util.StringUtil.empty;

@Slf4j
@Repository
public class UserDao {
    @Autowired
    private SystemConfig systemConfig;
    @Autowired
    private UserMapper userMapper;

    /**
     * 根据完整的用户实体添加用户到数据库
     *
     * @param user 用户实体
     */
    public void addUser(User user) {
        if (hasUser(user.getUsername())) {
            throw new SignInFailedException("用户名已存在");
        } else {
            userMapper.insert(user);
        }
    }

    /**
     * 注册用户到数据库
     *
     * @param username 用户名
     * @param password 密码
     * @param userType 用户类型
     */
    public User addUser(String username, String password, Integer userType) throws InvalidInputException {

        User user = createUser(username, password, userType);

        userMapper.insert(user);

        return user;
    }

    /**
     * 仅仅创建并返回用户
     *
     * @param username 用户名
     * @param password 密码
     * @param userType 用户类型
     * @return 创建的用户
     */

    public User createUser(String username, String password, Integer userType) throws InvalidInputException {

        if (empty(username) || empty(password)) {
            throw new InvalidInputException("用户名或密码不能为空");
        }

        if (!username.matches("^[a-zA-Z0-9_\\-]*$") || username.length() < 6 || username.length() > 30) {
            throw new InvalidInputException("用户名字符集仅限于大小写字母,数字,减号,以及下划线,并且长度大于等于6,小于等于30");
        }
        if (!password.matches("^[ -~]*$") || password.length() < 6 || password.length() > 20) {
            throw new InvalidInputException("密码字符集仅限于ASCII所有可见字符以及空格,并且长度大于等于6,小于等于20");
        }

        if (hasUser(username)) {
            throw new SignInFailedException("用户名已存在");
        }

        User user = new User();
        String userId = getUid();

        user.setUserId(userId);
        user.setUsername(username);
        user.setPasswordHash(Security.passwordHash(password, userId));
        user.setUserType(userType);
        user.setCreatedAt(new Date());
        user.setStorageLimit(systemConfig.getStorageSpace());
        user.setStatus(USER_OK);
        user.setLoginAttempts(0);
        user.setEmail(null);

        return user;
    }

    /**
     * 根据用户名和密码获取用户,不校验用户状态
     *
     * @param userName 用户名
     * @param password 密码
     * @return 用户, 结果可能为null,代表用户不存在或者密码错误
     */
    public User getUserByLogin(String userName, String password) {

        if (empty(userName) || empty(password)) {
            return null;
        }

        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(userName);
        List<User> users = userMapper.selectByExample(userExample);

        if (users == null || users.size() == 0) {
            return null;
        }

        if (users.size() > 1) {
            log.error("用户数量大于1");
            throw new SystemFatalException("同一用户名数量大于1");
        }

        User user = users.get(0);

        //验证密码
        if (user.getPasswordHash().equals(Security.passwordHash(password, user.getUserId()))) {
            return user;
        } else {
            return null;
        }
    }

    /**
     * 判断用户名是否存在
     *
     * @param username 用户名
     * @return 是否存在
     */

    public boolean hasUser(String username) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(username);
        List<User> users = userMapper.selectByExample(userExample);

        return users != null && users.size() >= 1;
    }
}
