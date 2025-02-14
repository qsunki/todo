package com.example.mytodo.user.application.exception;

public class UserDuplicateException extends RuntimeException {
    public UserDuplicateException(String message) {
        super(message);
    }
}
