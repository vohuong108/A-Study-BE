package com.example.astudy.dtos;

import com.example.astudy.enums.CourseContentStatus;
import com.example.astudy.enums.CourseContentType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Data
public class WeekContentDto {
    private Long ID;
    private String name;
    private Integer contentOrder;
    private CourseContentStatus contentStatus;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            timezone = "UTC"
    )
    private Date releaseDate;
    private CourseContentType contentType;
    private String contentPath;
    private Long weekId;
    private String url;
}
