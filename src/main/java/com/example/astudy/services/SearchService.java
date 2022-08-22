package com.example.astudy.services;

import com.example.astudy.dtos.SearchCourseResponse;

public interface SearchService {
    SearchCourseResponse searchCourse(String query);
}
