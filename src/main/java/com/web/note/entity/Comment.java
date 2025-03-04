package com.web.note.entity;

import lombok.Data;

import java.util.Date;
@Data
public class Comment {
    private String commentId;
    private String userId;
    private String targetDocument;
    private String commentContent;
    private Date createdAt;
}