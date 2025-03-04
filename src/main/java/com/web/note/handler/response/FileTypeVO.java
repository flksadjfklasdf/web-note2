package com.web.note.handler.response;

import com.web.note.entity.FileType;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class FileTypeVO {
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    @Size(min = 1,max = 20)
    public String typeField;
    @NotNull
    @Size(min = 1,max = 20)
    public String typeName;
    @NotNull
    @Size(min = 1,max = 20)
    public String contentType;

    public FileTypeVO(FileType fileType) {
        this.typeField = fileType.getTypeNameEn();
        this.typeName = fileType.getTypeNameCn();
    }
}
