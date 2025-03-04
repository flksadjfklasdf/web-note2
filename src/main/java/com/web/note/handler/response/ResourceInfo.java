package com.web.note.handler.response;

import lombok.Data;

@Data
public class ResourceInfo {
    public Integer totalCollections;
    public Integer totalArticles;
    public Integer totalImages;
    public Integer totalFiles;
    public Integer totalImagesSize;
    public Integer totalFilesSize;
    public Integer usedSpace;
    public Integer spaceLimit;
}
