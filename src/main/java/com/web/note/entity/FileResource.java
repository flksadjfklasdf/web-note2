package com.web.note.entity;

import lombok.Data;

import java.util.Date;

@Data
public class FileResource {
    public String fileId;
    public String userId;
    public String originalFilename;
    public String localFilename;
    public Integer fileSize;
    public Date uploadedAt;
    public Integer permission;
    public String authCode;
    public Integer fileStatus;
    public String fileMd5;
    public String fileType;
    public String fileNote;
}