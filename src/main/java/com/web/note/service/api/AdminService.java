package com.web.note.service.api;

import com.web.note.exception.InvalidInputException;
import com.web.note.handler.request.UserId;
import com.web.note.handler.request.UserIdAndCredence;
import com.web.note.handler.request.UsernameAndPassword;
import com.web.note.handler.response.UserToReturn;

import java.util.List;

public interface AdminService {
    void addAdmin(UsernameAndPassword user) throws InvalidInputException;

    void disableAdmin(UserId userId);

    void deleteAdmin(UserId userId);

    void modifyAdmin(UserIdAndCredence user);

    List<UserToReturn> getBusinessAdmin();

    UserToReturn getSystemAdmin();
}
