package astudy.services;

import astudy.dtos.CourseDto;
import astudy.dtos.WeekDto;
import astudy.response.EditCourseResponse;
import astudy.response.OverviewCourse;
import astudy.response.SearchCourseResponse;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface CourseService {
    CourseDto createCourse(CourseDto courseDto, String username);

    OverviewCourse enrollCourse(String username, Long courseId);

    OverviewCourse findOverviewCourse(Long courseId, String username);

    List<CourseDto> findAllCourse(String username);

    EditCourseResponse findEditCourseById(Long courseId, String username);

    void deleteCourseById(Long courseId);

    WeekDto createWeek(WeekDto newWeek);

    List<SearchCourseResponse> searchCourse(String query);

}
