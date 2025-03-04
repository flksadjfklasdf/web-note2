package com.web.note.handler.response;


public class DocumentFile {
    private String documentName;
    private String content;

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        String illegalChars = "[\\\\/:*?\"<>|]";
        this.documentName = documentName.replaceAll(illegalChars, "X");
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
