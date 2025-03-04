package com.web.note.handler.response;

import lombok.Data;

@Data
public class ImageDocument {
    public String documentId;
    public String documentName;
    public int imageCount;
    public int sizeCount;
}
