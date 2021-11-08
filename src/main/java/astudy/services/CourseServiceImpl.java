package astudy.services;

import astudy.dtos.CourseDto;
import astudy.models.Category;
import astudy.models.Course;
import astudy.models.User;
import astudy.repositories.CategoryRepository;
import astudy.repositories.CourseRepository;
import astudy.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService{
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public CourseDto createCourse(CourseDto courseDto) {
        Course course = new Course();
        StringBuilder convertedSkill = new StringBuilder();
        StringBuilder convertedLearn = new StringBuilder();
        String username = courseDto.getUsername();
        String categoryName = courseDto.getCategory();

        for (String str : courseDto.getSkillInfo()) {
            convertedSkill.append(str).append(" | ");
        }
        for (String str : courseDto.getLearnInfo()) {
            convertedLearn.append(str).append(" | ");
        }

        Category resultCategory = categoryRepository.findByName(categoryName);
        User currentUser = userRepository.findUserByUsername(username);

        if (resultCategory == null) {
            resultCategory = categoryRepository.save(new Category(categoryName));
        }

        if(currentUser == null) {
            return null;
        }

        course.setName(courseDto.getName());
        course.setDescription(courseDto.getDescription());
        course.setSkillInfo(convertedSkill.toString());
        course.setLearnInfo(convertedLearn.toString());
        course.setCategory(resultCategory);
        course.setAuthor(currentUser);
        course.setReleaseDate(new Date());

        Course result = courseRepository.save(course);

        courseDto.setAuthor(result.getAuthor().getUsername());
        courseDto.setReleaseDate(result.getReleaseDate());

        return courseDto;

    }

    @Override
    public CourseDto findCourseById(Long courseId) {
        Course result = courseRepository.getById(courseId);
        CourseDto response = new CourseDto();

        List<String> convertedSkill = new ArrayList<>(Arrays.asList(result.getSkillInfo().split(" | ")));
        List<String> convertedLearn = new ArrayList<>(Arrays.asList(result.getLearnInfo().split(" | ")));

        response.setName(result.getName());
        response.setDescription(result.getDescription());
        response.setSkillInfo(convertedSkill);
        response.setLearnInfo(convertedLearn);
        response.setCategory(result.getCategory().getName());
        response.setAuthor(result.getAuthor().getUsername());
        response.setReleaseDate(result.getReleaseDate());

        return response;
    }

    @Override
    public void deleteCourseById(Long courseId) {
        courseRepository.deleteById(courseId);
    }

    @Override
    public CourseDto updateCourseById(Long courseId, CourseDto courseUpdate) {
        return null;
    }
}
