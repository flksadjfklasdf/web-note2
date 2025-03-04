package com.web.note.exception;

public class LoginException extends RuntimeException{

    public LoginException(int errorType){

    }

    public LoginException() {
        super("用户名或者密码错误");
    }

    public LoginException(String message) {
        super(message);
    }

    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginException(Throwable cause) {
        super(cause);
    }
}
