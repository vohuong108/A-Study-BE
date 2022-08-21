package com.example.astudy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


@Data
public class WeekDto {
    @JsonProperty("id")
    private Long ID;
    @JsonProperty("weekOrder")
    private int weekOrder;
    @JsonProperty("name")
    private String name;

    @JsonProperty("courseId")
    private Long courseId;

    @JsonProperty("contents")
    List<WeekContentDto> contents;
}
