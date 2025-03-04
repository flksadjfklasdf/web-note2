package com.web.note.entity;

import lombok.Data;

import java.util.Date;
@Data
public class Collection {
    private String collectionId;
    private String userId;
    private String collectionName;
    private String collectionType;
    private Boolean isPublic;
    private Date createdAt;
    private String collectionDescription;
}