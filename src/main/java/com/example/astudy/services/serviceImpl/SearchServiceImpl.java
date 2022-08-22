package com.example.astudy.services.serviceImpl;

import com.example.astudy.dtos.CourseDto;
import com.example.astudy.dtos.SearchCourseResponse;
import com.example.astudy.entities.Course;
import com.example.astudy.entities.projections.SearchCourseProjection;
import com.example.astudy.repositories.CourseRepo;
import com.example.astudy.services.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SearchServiceImpl implements SearchService {
    private final CourseRepo courseRepo;
    @Override
    public SearchCourseResponse searchCourse(String query) {
        List<SearchCourseProjection> courseList = courseRepo.searchCourseByQuery(query);
        SearchCourseResponse response = new SearchCourseResponse();
        response.setTotalResult(courseList.size());
        response.setResults(courseList);
        return response;
    }
}
