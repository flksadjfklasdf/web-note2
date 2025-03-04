package com.web.note.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Application {
    private String appId;
    private String appUserId;
    private String appType;
    private String appValue;
    private String appContent;
    private Integer appStatus;
    private Date appSubmitDate;
    private Date appDealDate;
    private String appDealUserId;
    private String appDealUserName;
    private String appData;
}