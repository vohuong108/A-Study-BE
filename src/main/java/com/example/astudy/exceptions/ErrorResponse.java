package com.example.astudy.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    // customizing timestamp serialization format
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;
    private int code;
    private String status;
    private String message;
    private String errorType;
    private String path;
    private String stackTrace;
    private Object data;

    public ErrorResponse() {
        timestamp = new Date();
    }

    public ErrorResponse(
            HttpStatus httpStatus,
            String message,
            String path
    ) {
        this();

        this.code = httpStatus.value();
        this.status = httpStatus.name();
        this.message = message;
        this.path = path;
    }

    public ErrorResponse(
            HttpStatus httpStatus,
            String message,
            String errorType,
            String path
    ) {
        this();

        this.code = httpStatus.value();
        this.status = httpStatus.name();
        this.errorType = errorType;
        this.message = message;
        this.path = path;
    }
}
