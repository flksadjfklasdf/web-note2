package com.web.note.handler.request;

import com.web.note.handler.valid.AuthCodeValid;
import com.web.note.handler.valid.DatabaseVarcharValid;
import com.web.note.handler.valid.IdValid;
import lombok.Data;

@Data
public class FileModify {
    @IdValid
    public String fileId;
    public Boolean isPublic;
    public Boolean isAuthCode;
    @AuthCodeValid
    public String authCode;
    @DatabaseVarcharValid
    public String fileNote;
    public String fileType;
}
