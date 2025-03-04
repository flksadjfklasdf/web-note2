package com.web.note.handler.response;

import lombok.Data;

import java.util.Map;

@Data
public class CustomerInfo {
    private Map<String,String> configInfo;
}
