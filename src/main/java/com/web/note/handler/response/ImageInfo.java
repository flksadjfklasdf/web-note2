package com.web.note.handler.response;

import lombok.Data;

@Data
public class ImageInfo {
    public String imageId;
    public String imageName;
    public String imageCreateTime;
    public int size;
}