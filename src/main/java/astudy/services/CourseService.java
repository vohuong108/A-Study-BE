package astudy.services;

import astudy.dtos.CourseDto;
import org.springframework.stereotype.Service;
@Service
public interface CourseService {
    CourseDto createCourse(CourseDto courseDto);

    CourseDto findCourseById(Long courseId);

    void deleteCourseById(Long courseId);

    CourseDto updateCourseById(Long courseId, CourseDto courseUpdate);
}
