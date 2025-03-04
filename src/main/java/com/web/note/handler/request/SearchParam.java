package com.web.note.handler.request;

import lombok.Data;

@Data
public class SearchParam {
    public String username;
    public String email;
    public String status;
    public String sort;
    public Integer page;
}
