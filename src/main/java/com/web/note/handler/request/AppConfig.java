package com.web.note.handler.request;

import lombok.Data;

import javax.validation.Valid;

@Data
public class AppConfig {
    @Valid
    private UserConfig user;
}