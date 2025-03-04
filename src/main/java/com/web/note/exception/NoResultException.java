package com.web.note.exception;

public class NoResultException extends RuntimeException{
    public NoResultException() {
    }

    public NoResultException(String message) {
        super(message);
    }

    public NoResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoResultException(Throwable cause) {
        super(cause);
    }
}
