package com.web.note.exception;

public class SignInFailedException extends RuntimeException{
    public SignInFailedException() {
    }

    public SignInFailedException(String message) {
        super(message);
    }

    public SignInFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SignInFailedException(Throwable cause) {
        super(cause);
    }
}
