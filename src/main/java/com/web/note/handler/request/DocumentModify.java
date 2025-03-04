package com.web.note.handler.request;

import com.web.note.handler.valid.IdValid;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class DocumentModify {
    @IdValid
    public String documentId;
    @Size(min = 1,max = 20)
    public String documentName;
}
