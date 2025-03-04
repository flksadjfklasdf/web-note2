package com.web.note.exception;

public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException() {
        super("Unauthorized Access: You do not have permission to access this page.");
    }

    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
