package astudy.services;

import astudy.dtos.CourseDto;
import astudy.dtos.LectureDto;
import astudy.dtos.WeekDto;
import astudy.response.EditCourseResponse;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
public interface CourseService {
    CourseDto createCourse(CourseDto courseDto, String username);

    List<CourseDto> findAllCourse(String username);

    EditCourseResponse findEditCourseById(Long courseId);

    void deleteCourseById(Long courseId);

    WeekDto createWeek(WeekDto newWeek);

    LectureDto createLecture(LectureDto newLecture) throws IOException;

    LectureDto findLectureById(Long lectureId);
}
