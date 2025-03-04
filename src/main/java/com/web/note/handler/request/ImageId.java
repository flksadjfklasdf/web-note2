package com.web.note.handler.request;

import com.web.note.handler.valid.IdValid;
import lombok.Data;

@Data
public class ImageId {
    @IdValid
    public String imageId;
}
