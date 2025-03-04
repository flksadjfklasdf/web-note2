package com.web.note.handler.controller;

import com.web.note.exception.InvalidInputException;
import com.web.note.handler.request.UserId;
import com.web.note.handler.request.UserIdAndCredence;
import com.web.note.handler.request.UsernameAndPassword;
import com.web.note.handler.response.ResultEntity;
import com.web.note.handler.response.UserToReturn;
import com.web.note.security.RequireRole;
import com.web.note.security.Role;
import com.web.note.service.api.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.web.note.constant.StatusConstant.OK;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    public AdminService adminService;


    @RequireRole(value = Role.SYSTEM_ADMIN)
    @PostMapping("/add")
    public ResultEntity<Object> addAdmin(@RequestBody @Validated UsernameAndPassword user) throws InvalidInputException {
        adminService.addAdmin(user);

        return ResultEntity.resultOK();
    }
    @RequireRole(value = Role.SYSTEM_ADMIN)
    @RequestMapping("/disable")
    public ResultEntity<Object> disableAdmin(@RequestBody @Validated UserId userId) {
        adminService.disableAdmin(userId);

        return ResultEntity.resultOK();
    }
    @RequireRole(value = Role.SYSTEM_ADMIN)
    @RequestMapping("/delete")
    public ResultEntity<Object> deleteAdmin(@RequestBody @Validated UserId userId) {
        adminService.deleteAdmin(userId);

        return ResultEntity.resultOK();
    }
    @RequireRole(value = Role.SYSTEM_ADMIN)
    @RequestMapping("/modify")
    public ResultEntity<Object> modifyAdmin(@RequestBody @Validated UserIdAndCredence user){

        adminService.modifyAdmin(user);

        return ResultEntity.resultOK();
    }
    @RequireRole(value = Role.SYSTEM_ADMIN)
    @RequestMapping("/getSystemAdmin")
    public ResultEntity<UserToReturn> getSystemAdmin() {
        ResultEntity<UserToReturn> result=new ResultEntity<>();

        UserToReturn admin=adminService.getSystemAdmin();

        result.setStatus(OK);
        result.setData(admin);
        return result;
    }

    @RequireRole(value = Role.SYSTEM_ADMIN)
    @RequestMapping("/getBusinessAdmin")
    public ResultEntity<List<UserToReturn>> getBusinessAdmin() {
        ResultEntity<List<UserToReturn>> result=new ResultEntity<>();

        List<UserToReturn> admin=adminService.getBusinessAdmin();

        result.setStatus(OK);
        result.setData(admin);
        return result;
    }
}
