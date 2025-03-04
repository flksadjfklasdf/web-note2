package com.web.note.handler.response;

import lombok.Data;

import java.util.List;

@Data
public class SearchApplicationEntity {
    public List<ApplicationToReturn> apps;
    public Integer totalPages;
}
