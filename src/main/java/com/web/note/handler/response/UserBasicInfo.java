package com.web.note.handler.response;

import lombok.Data;

import java.util.Date;

@Data
public class UserBasicInfo {
    private String username;
    private String userRole;
    private int collectionCount;
    private int articleCount;
    private int totalFileSize;
    private int totalAvailableSpace;
    private Date registrationDate;
}
