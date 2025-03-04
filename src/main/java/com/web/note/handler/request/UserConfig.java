package com.web.note.handler.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Data
public class UserConfig {
    @Min(value = 0, message = "无效的数值")
    @Max(value = 2, message = "无效的数值")
    private int register;
    @Min(value = 1, message = "无效的数值")
    private int space;
    @Pattern(regexp = "^(GB|MB)$", message = "字段值只能为GB、MB")
    private String unit;
}