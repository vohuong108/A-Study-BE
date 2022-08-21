package com.example.astudy.dtos;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CourseDto {
    private Long ID;
    private String name;
    private String description;
    private Date releaseDate;
    private String category;

    private Date enrolledAt;
    private Integer progress;

    private List<String> skills;
    private List<String> learns;
    private String author;
}
