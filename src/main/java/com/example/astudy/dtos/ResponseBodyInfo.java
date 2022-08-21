package com.example.astudy.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ResponseBodyInfo {
    // customizing timestamp serialization format
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;
    private String path;
    private String message;
    private Object data;

    public ResponseBodyInfo() {
        timestamp = new Date();
    }

    public ResponseBodyInfo(
            String path,
            Object data
    ) {
        this();
        this.path = path;
        this.data = data;

    }

    public ResponseBodyInfo(
            String path,
            String message
    ) {
        this();
        this.path = path;
        this.message = message;

    }
}
