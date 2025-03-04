package com.web.note.handler.response;

import lombok.Data;

import java.util.Date;

@Data
public class ApplicationToReturn {
    public String appId;
    public String username;
    public String appType;
    public Integer appStatus;
    public String appValue;
    public Date appSubmitDate;
    public Date dealDate;
    public String dealUser;
}
