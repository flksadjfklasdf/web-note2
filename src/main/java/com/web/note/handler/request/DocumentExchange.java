package com.web.note.handler.request;

import com.web.note.handler.valid.IdValid;
import lombok.Data;

@Data
public class DocumentExchange {
    @IdValid
    public String documentId1;
    @IdValid
    public String documentId2;
}
