package com.web.note.handler.response;

import com.web.note.cache.FileTypeCache;
import com.web.note.entity.FileResource;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FileResourceVO extends FileResource {
    private String fileTypeName;

    public FileResourceVO(FileResource fileResource){

        this.fileId=fileResource.fileId;
        this.userId=fileResource.userId;
        this.originalFilename=fileResource.originalFilename;
        this.fileSize=fileResource. fileSize;
        this.uploadedAt=fileResource.uploadedAt;
        this.permission=fileResource. permission;
        this.authCode=fileResource.authCode;
        this.fileMd5=fileResource.fileMd5;
        this.fileType=fileResource.fileType;
        this.fileNote=fileResource.fileNote;

        String contentType = FileTypeCache.getInstance().getFileTypeName(fileType);
        this.fileTypeName = contentType == null ? "默认" : contentType;
    }
}