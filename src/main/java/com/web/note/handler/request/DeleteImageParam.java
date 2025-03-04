package com.web.note.handler.request;

import com.web.note.handler.valid.IdValid;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class DeleteImageParam {
    @Max(value = 3)
    @Min(value = 1)
    public int type;
    @IdValid
    public String itemId;
}
