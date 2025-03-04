package com.web.note.handler.request;

import com.web.note.handler.valid.DatabaseVarcharValid;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class CollectionCreate {
    @NotNull
    @Pattern(regexp = "markdown|html")
    public String collectionType;
    @Size(min = 1,max = 20)
    public String CollectionName;
    @DatabaseVarcharValid
    public String collectionDescription;
    @NotNull
    public Boolean isPublic;
}
