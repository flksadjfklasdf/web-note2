package com.web.note.handler.request;

import com.web.note.handler.valid.IdValid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentId {
    @IdValid
    public String documentId;
}
