package com.web.note.handler.response;

import lombok.Data;

import java.util.Date;
@Data
public class UserToShow {
    public String userId;
    public String username;
    public Date createdAt;
    public Date lastLoginAt;
    public Integer status;
}
