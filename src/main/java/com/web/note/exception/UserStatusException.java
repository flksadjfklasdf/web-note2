package com.web.note.exception;

import com.web.note.entity.User;

public class UserStatusException extends RuntimeException {
    public static final int LOGIN_TOO_MANY_TIMES = 10;


    private Integer status;
    private User user;

    public UserStatusException(Integer status, User user) {
        super("用户状态异常");
        this.status = status;
        this.user = user;
    }

    public UserStatusException() {
        super();
    }

    public UserStatusException(String message) {
        super(message);
    }

    public UserStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserStatusException(Throwable cause) {
        super(cause);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}