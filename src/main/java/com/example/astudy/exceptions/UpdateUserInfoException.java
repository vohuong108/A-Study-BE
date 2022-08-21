package com.example.astudy.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UpdateUserInfoException extends RuntimeException{
    public UpdateUserInfoException(String message) {
        super(message);
    }

    public UpdateUserInfoException(String message, Throwable cause) {
        super(message, cause);
    }
}
