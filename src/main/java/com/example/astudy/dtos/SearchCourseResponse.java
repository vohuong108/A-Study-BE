package com.example.astudy.dtos;

import com.example.astudy.entities.projections.SearchCourseProjection;
import lombok.Data;

import java.util.List;

@Data
public class SearchCourseResponse {
    private Integer totalResult;
    private List<SearchCourseProjection> results;
}
