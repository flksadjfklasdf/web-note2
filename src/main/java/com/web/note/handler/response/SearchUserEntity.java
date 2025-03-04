package com.web.note.handler.response;

import lombok.Data;

import java.util.List;

@Data
public class SearchUserEntity {
    public List<UserToShow> users;
    public Integer totalPages;
}
