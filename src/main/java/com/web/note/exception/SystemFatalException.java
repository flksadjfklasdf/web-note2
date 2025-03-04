package com.web.note.exception;

public class SystemFatalException extends RuntimeException {
    public SystemFatalException() {
        super();
    }

    public SystemFatalException(String message) {
        super(message);
    }

    public SystemFatalException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemFatalException(Throwable cause) {
        super(cause);
    }
}

