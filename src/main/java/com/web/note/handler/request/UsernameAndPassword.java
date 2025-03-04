package com.web.note.handler.request;

import com.web.note.handler.valid.PasswordValid;
import com.web.note.handler.valid.UsernameValid;
import lombok.Data;

@Data
public class UsernameAndPassword {
    @UsernameValid
    public String username;
    @PasswordValid
    public String password;
}
