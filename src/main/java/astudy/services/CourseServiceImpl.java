package astudy.services;

import astudy.dtos.CourseDto;
import astudy.dtos.LectureDto;
import astudy.dtos.WeekDto;
import astudy.models.*;
import astudy.repositories.*;
import astudy.response.EditCourseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService{
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final WeekRepository weekRepository;
    private final WeekService weekService;

    @Override
    public CourseDto createCourse(CourseDto courseDto, String username) {
        Course course = new Course();
        StringBuilder convertedSkill = new StringBuilder();
        StringBuilder convertedLearn = new StringBuilder();
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
        courseDto.setCourseId(result.getID());

        return courseDto;

    }

    @Override
    public List<CourseDto> findAllCourse(String username) {
        User userDb = userRepository.findUserByUsername(username);
        List<CourseDto> courses = userDb.getCourses().stream().map(course -> {
            CourseDto temp = new CourseDto();
            temp.setCourseId(course.getID());
            temp.setName(course.getName());
            temp.setAuthor(course.getAuthor().getUsername());

            return temp;
        }).collect(Collectors.toList());

        return courses;
    }

    @Override
    public EditCourseResponse findEditCourseById(Long courseId) {
        Course courseDb = courseRepository.findCourseById(courseId);
        log.info("end get edit: {}", courseDb.toString());

        List<WeekDto> weeks = courseDb.getWeeks().stream().map(week -> {
            WeekDto weekDto = new WeekDto();
            weekDto.setWeekId(week.getID());
            weekDto.setName(week.getName());
            weekDto.setSerialWeek(week.getSerialWeek());

            //TODO text and video
            List<LectureDto> lectures = week.getLectures().stream().map(lecture -> {
                LectureDto lectureDto = new LectureDto();
                lectureDto.setLectureId(lecture.getID());
                lectureDto.setTitle(lecture.getTitle());
                lectureDto.setLectureType(lecture.getLectureType().toString());
                lectureDto.setLectureStatus(lecture.getLectureStatus().toString());
                lectureDto.setIndexLecture(lecture.getIndexLecture());
                lectureDto.setUrl("/week/lecture/"
                        + lecture.getLectureType().toString().toLowerCase()
                        + "/" + lecture.getID());

                return lectureDto;
            }).collect(Collectors.toList());

            //TODO quiz
            List<LectureDto> quizs = week.getQuizs().stream().map(quiz -> {
                LectureDto quizDto = new LectureDto();

                quizDto.setLectureId(quiz.getID());
                quizDto.setTitle(quiz.getTitle());
                quizDto.setLectureType("QUIZ");
                quizDto.setLectureStatus(quiz.getQuizStatus().toString());
                quizDto.setIndexLecture(quiz.getIndexLecture());
                quizDto.setUrl("/week/lecture/quiz/" + quiz.getID());

                return quizDto;

            }).collect(Collectors.toList());

            lectures.addAll(quizs);

            weekDto.setLectures(lectures);
            return weekDto;
        }).collect(Collectors.toList());

        EditCourseResponse response = new EditCourseResponse();
        response.setID(courseDb.getID());
        response.setName(courseDb.getName());
        response.setDescription(courseDb.getDescription());
        response.setCategory(courseDb.getCategory().getName());
        response.setWeeks(weeks);
        return response;
    }

    @Override
    public WeekDto createWeek(WeekDto newWeek) {
        Course courseDb = courseRepository.findCourseById(newWeek.getCourseId());

        Week createWeek = new Week();
        createWeek.setName(newWeek.getName());
        createWeek.setSerialWeek(newWeek.getSerialWeek());
        createWeek.setCourse(courseDb);

        Week result = weekRepository.save(createWeek);
        return newWeek;
    }

    @Override
    public void deleteCourseById(Long courseId) {
        List<Long> listWeekId = weekRepository.getListIdByCourseId(courseId);

        for (Long weekId : listWeekId) {
            weekService.deleteWeekById(weekId);
        }

        courseRepository.deleteById(courseId);
    }
}
