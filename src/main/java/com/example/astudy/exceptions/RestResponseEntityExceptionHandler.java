package com.example.astudy.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler {
    @ExceptionHandler(value = {RequestFieldNotFoundException.class})
    protected ResponseEntity<Object> handleRequestFieldNotFound(
            RequestFieldNotFoundException ex, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        String uri = ((ServletWebRequest)request).getRequest().getRequestURI();
        return ResponseEntity
                .status(status)
                .headers(new HttpHeaders())
                .body(new ErrorResponse(status, ex.getMessage(), uri));
    }

    @ExceptionHandler(value = {TokenInvalid.class})
    protected ResponseEntity<Object> handleTokenInvalid(
            TokenInvalid ex, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        String uri = ((ServletWebRequest)request).getRequest().getRequestURI();
        return ResponseEntity
                .status(status)
                .headers(new HttpHeaders())
                .body(new ErrorResponse(status, ex.getMessage(), uri));

    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    protected ResponseEntity<Object> handleUsernameNotFoundException(
            UsernameNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        String uri = ((ServletWebRequest)request).getRequest().getRequestURI();
        return ResponseEntity
                .status(status)
                .headers(new HttpHeaders())
                .body(new ErrorResponse(status, ex.getMessage(), uri));

    }

    @ExceptionHandler(value = {SignUpException.class})
    protected ResponseEntity<Object> handleSignUpException(
        SignUpException ex, WebRequest request
    ) {
        HttpStatus status = HttpStatus.CONFLICT;
        String uri = ((ServletWebRequest)request).getRequest().getRequestURI();

        return ResponseEntity
                .status(status)
                .headers(new HttpHeaders())
                .body(new ErrorResponse(status, ex.getMessage(), uri));
    }

    @ExceptionHandler(value = {UpdateUserInfoException.class})
    protected ResponseEntity<Object> handleUpdateUserInfoException(
            UpdateUserInfoException ex, WebRequest request
    ) {
        HttpStatus status = HttpStatus.CONFLICT;
        String uri = ((ServletWebRequest)request).getRequest().getRequestURI();

        return ResponseEntity
                .status(status)
                .headers(new HttpHeaders())
                .body(new ErrorResponse(status, ex.getMessage(), uri));
    }

    @ExceptionHandler(value = {QuizSessionException.class})
    protected ResponseEntity<Object> handleQuizSessionException(
            QuizSessionException ex, WebRequest request
    ) {
        String uri = ((ServletWebRequest)request).getRequest().getRequestURI();

        return ResponseEntity
                .status(ex.status)
                .headers(new HttpHeaders())
                .body(new ErrorResponse(ex.status, ex.getMessage(), uri));
    }



}
