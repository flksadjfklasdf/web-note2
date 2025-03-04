package com.web.note.handler.request;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class AppSearchParam {
    public String status;
    @Min(value = 1, message = "无效的数值")
    public Integer page;
}
