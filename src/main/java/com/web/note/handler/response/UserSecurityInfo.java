package com.web.note.handler.response;

import lombok.Data;

import java.util.Date;

@Data
public class UserSecurityInfo {
    private String username;
    private String email;
    private String ip;
    private String lastIp;
    private Date lastLogin;
}
