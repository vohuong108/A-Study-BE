package com.example.astudy.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CourseContentDto {
    private Long ID;
    private String name;

    List<WeekDto> weeks;
}
