package com.web.note.handler.request;

import com.web.note.handler.valid.PasswordValid;
import lombok.Data;

@Data
public class UserPassword {
    @PasswordValid
    private String password;
}
