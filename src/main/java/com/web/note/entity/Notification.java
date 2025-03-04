package com.web.note.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Notification {
    private String notificationId;
    private String notificationUserId;
    private Date notificationTime;
    private Integer notificationStatus;
    private String notificationContent;
}