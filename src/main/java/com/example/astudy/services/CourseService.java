package com.example.astudy.services;

import com.example.astudy.dtos.*;


import java.io.IOException;
import java.util.List;

public interface CourseService {
    EnrollCourseResponse enrollCourse(Long courseId);
    CourseDto getCourseInfoById(Long courseId);
    List<CourseDto> getAllCourseOfUser(String username);
    CourseDto save(CourseDto course);
    void deleteCourse(Long courseId);
    CourseContentDto getCourseContentById(Long courseId);
    WeekDto saveWeek(Long courseId, WeekDto weekDto);
    WeekDto renameWeek(Long courseId, Long weekId, String name);
    WeekContentDto createLectureContent(Long courseId, Long weekId, LectureContentFormData formData) throws IOException;
    void deleteWeekContent(Long courseId, Long weekId, Long contentId);
    WeekContentDto getLectureContentUrl(Long courseId, Long weekId, Long contentId);
    WeekContentDto updateLectureContent(Long courseId, Long weekId, Long contentId, LectureContentFormData formData) throws IOException;
    List<CourseDto> getAllCourseInSystem(int pageNumber);
}
