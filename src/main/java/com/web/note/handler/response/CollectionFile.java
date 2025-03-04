package com.web.note.handler.response;


import java.util.List;

public class CollectionFile {
    private String collectionName;
    private List<DocumentFile> documentFileList;

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        String illegalChars = "[\\\\/:*?\"<>|]";
        this.collectionName = collectionName.replaceAll(illegalChars, "X");
    }

    public List<DocumentFile> getDocumentFileList() {
        return documentFileList;
    }

    public void setDocumentFileList(List<DocumentFile> documentFileList) {
        this.documentFileList = documentFileList;
    }
}
