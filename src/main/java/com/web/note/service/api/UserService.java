package com.web.note.service.api;

import com.web.note.entity.User;
import com.web.note.exception.InvalidInputException;
import com.web.note.exception.SignInFailedException;
import com.web.note.handler.request.SearchParam;
import com.web.note.handler.request.UserIdAndPassword;
import com.web.note.handler.request.UsernameAndPassword;
import com.web.note.handler.response.SearchUserEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface UserService {



    User getUserByLogin(String userName, String password);

    void signIn(String userName, String password) throws SignInFailedException, InvalidInputException;

    void setLoginInformation(User userByLogin, HttpServletRequest request);

    void initSystemAdmin(User admin, UsernameAndPassword param);

    SearchUserEntity getUsersList(SearchParam param);

    int enableUser(List<String> userIds);

    int disableUser(List<String> userIds);

    int deleteUser(List<String> userIds);

    int modifyUserPassword(UserIdAndPassword param);

    void addUser(UsernameAndPassword param) throws InvalidInputException;

    String getUserNameById(String appUserId);

    void initUser(User user);
}
