package com.web.note.handler.response;

import lombok.Data;

import java.util.Date;

@Data
public class UserToReturn {
    public String userId;
    public String username;
    public Date createdAt;
    public Integer status;
    public Integer userType;
}
