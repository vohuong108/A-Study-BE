package astudy.services;

import astudy.dtos.CourseDto;
import astudy.dtos.LectureDto;
import astudy.dtos.WeekDto;
import astudy.enums.LectureStatus;
import astudy.enums.LectureType;
import astudy.models.*;
import astudy.repositories.*;
import astudy.response.EditCourseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
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
    private final LectureRepository lectureRepository;

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
        Course courseDb = courseRepository.getById(courseId);
        EditCourseResponse response = new EditCourseResponse();

        response.setID(courseDb.getID());
        response.setName(courseDb.getName());
        response.setDescription(courseDb.getDescription());
        response.setCategory(courseDb.getCategory().getName());
        List<WeekDto> weeks = courseDb.getWeeks().stream().map(week -> {
            WeekDto weekDto = new WeekDto();
            weekDto.setName(week.getName());
            weekDto.setSerialWeek(week.getSerialWeek());
            //TODO lecture dto
            return weekDto;
        }).collect(Collectors.toList());

        response.setWeeks(weeks);

        return response;
    }

    @Override
    public void deleteCourseById(Long courseId) {
        courseRepository.deleteById(courseId);
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
    public LectureDto createLecture(LectureDto newLecture) throws IOException {
        Week weekDb = weekRepository.getById(newLecture.getWeekId());

        log.info("getLectureStatus: {}", newLecture.getLectureStatus());
        log.info("getIndexLecture: {}", newLecture.getIndexLecture());
        log.info("getLectureType: {}", newLecture.getLectureType());
        log.info("getWeekId: {}", newLecture.getWeekId());
        log.info("getTitle: {}", newLecture.getTitle());
        log.info("getOriginalFilename: {}", newLecture.getFile().getOriginalFilename());

        Lecture createLec = new Lecture();
        createLec.setIndexLecture(newLecture.getIndexLecture());
        createLec.setTitle(newLecture.getTitle());
        createLec.setWeek(weekDb);
        createLec.setReleaseDate(new Date());
        createLec.setContent(newLecture.getFile().getBytes());

        String statusNewLec = newLecture.getLectureStatus().toUpperCase();

        if(LectureStatus.PRIVATE.toString().equals(statusNewLec)) {
            createLec.setLectureStatus(LectureStatus.PRIVATE);
        } else {
            createLec.setLectureStatus(LectureStatus.PUBLIC);
        }

        String NewLecType = newLecture.getLectureType().toUpperCase();

        if(LectureType.TEXT.toString().equals(NewLecType)) {
            createLec.setLectureType(LectureType.TEXT);
        } else {
            createLec.setLectureType(LectureType.VIDEO);
        }

        lectureRepository.save(createLec);

        return newLecture;

    }

    @Override
    public LectureDto findLectureById(Long lectureId) {
        Lecture lecDb = lectureRepository.findByWeekIdAndLectureId(lectureId);

        if (lecDb != null) {
            LectureDto result = new LectureDto();

            result.setLectureId(lecDb.getID());
            result.setIndexLecture(lecDb.getIndexLecture());
            result.setLectureStatus(lecDb.getLectureStatus().toString());
            result.setLectureType(lecDb.getLectureType().toString());
            result.setTitle(lecDb.getTitle());
            result.setWeekId(lecDb.getWeek().getID());
            result.setUrl(
                    ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/course/lecture/")
                    .path(lecDb.getID().toString()).toUriString());
            result.setContent(lecDb.getContent());
            return result;
        } else return null;

    }
}
