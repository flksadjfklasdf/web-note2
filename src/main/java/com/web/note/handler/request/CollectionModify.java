package com.web.note.handler.request;

import lombok.Data;

@Data
public class CollectionModify {
    public String collectionId;
    public String collectionName;
    public String collectionDescription;
    public Boolean IsPublic;
}
