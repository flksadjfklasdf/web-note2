package com.web.note.handler.response;

import lombok.Data;

@Data
public class ImageCollection {
    public String collectionId;
    public String collectionName;
    public int imageCount;
    public int sizeCount;
}