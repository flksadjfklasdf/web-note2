package com.web.note.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private String userId;
    private String username;
    private String passwordHash;
    private String email;
    private Integer userType;
    private Integer status;
    private Date createdAt;
    private String profilePictureId;
    private String lastLoginIp;
    private Date lastLoginAt;
    private Integer loginAttempts;
    private Integer storageLimit;
}