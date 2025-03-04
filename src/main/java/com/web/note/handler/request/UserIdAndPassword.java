package com.web.note.handler.request;

import com.web.note.handler.valid.IdValid;
import com.web.note.handler.valid.PasswordValid;
import lombok.Data;

@Data
public class UserIdAndPassword {
    @IdValid
    public String userId;
    @PasswordValid
    public String password;
}
