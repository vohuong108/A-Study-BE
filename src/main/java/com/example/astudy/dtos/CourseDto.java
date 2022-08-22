package com.example.astudy.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CourseDto {
    private Long ID;
    private String name;
    private String description;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            timezone = "UTC"
    )
    private Date releaseDate;
    private String category;

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            timezone = "UTC"
    )
    private Date enrolledAt;
    private Integer progress;

    private List<String> skills;
    private List<String> learns;
    private String author;
    private Boolean isAccess;
}
