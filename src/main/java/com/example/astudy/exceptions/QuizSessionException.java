package com.example.astudy.exceptions;

import org.springframework.http.HttpStatus;

public class QuizSessionException extends RuntimeException {
    public HttpStatus status = HttpStatus.TOO_MANY_REQUESTS;
    public QuizSessionException(String message) {
        super(message);
    }

    public QuizSessionException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public QuizSessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuizSessionException(Throwable cause) {
        super(cause);
    }

    public QuizSessionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
