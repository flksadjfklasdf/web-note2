package com.web.note.handler.request;

import com.web.note.handler.valid.IdValid;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class DocumentCreate {
    @IdValid
    public String collectionId;
    @Size(min = 1,max = 20)
    public String documentName;
}
