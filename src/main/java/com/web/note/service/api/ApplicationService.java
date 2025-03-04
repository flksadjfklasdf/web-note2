package com.web.note.service.api;

import com.web.note.entity.User;
import com.web.note.handler.request.AppSearchParam;
import com.web.note.handler.response.SearchApplicationEntity;

import java.util.List;

public interface ApplicationService {
    void submitRegisterApplication(User user);

    SearchApplicationEntity search(AppSearchParam searchParam);

    void permitAll(User dealUser, List<String> appIds);

    void denyAll(User dealUser, List<String> appIds);
}
