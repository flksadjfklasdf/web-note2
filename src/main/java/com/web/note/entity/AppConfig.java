package com.web.note.entity;

import lombok.Data;

@Data
public class AppConfig {
    private String configItem;
    private String configName;
    private String configValue;
    private String configRange;
}