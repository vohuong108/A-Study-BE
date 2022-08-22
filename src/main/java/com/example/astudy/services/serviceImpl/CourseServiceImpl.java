package com.example.astudy.services.serviceImpl;

import com.example.astudy.config.security.share.ValidateCourseResource;
import com.example.astudy.config.security.utils.SecurityUtils;
import com.example.astudy.dtos.*;
import com.example.astudy.dtos.mapper.CourseMapper;
import com.example.astudy.dtos.mapper.WeekContentMapper;
import com.example.astudy.dtos.mapper.WeekMapper;
import com.example.astudy.entities.*;
import com.example.astudy.entities.key.StudentCourseKey;
import com.example.astudy.enums.AccountStatus;
import com.example.astudy.enums.CourseContentType;
import com.example.astudy.enums.CourseInfoType;
import com.example.astudy.exceptions.RequestFieldNotFoundException;
import com.example.astudy.repositories.*;
import com.example.astudy.services.CourseService;
import com.example.astudy.services.storage.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {
    private final int COURSE_PAGE_SIZE = 25;
    private final CourseRepo courseRepo;
    private final WeekRepo weekRepo;
    private final StudentCourseRepo studentCourseRepo;
    private final CategoryRepo categoryRepo;
    private final UserRepo userRepo;
    private final WeekContentRepo weekContentRepo;
    private final LectureRepo lectureRepo;
    private final AmazonS3Service amazonS3Service;
    private final CourseMapper courseMapper;
    private final WeekMapper weekMapper;
    private final WeekContentMapper weekContentMapper;
    private final ValidateCourseResource validateCourseResource;

    @Override
    public EnrollCourseResponse enrollCourse(Long courseId) {
        String username = SecurityUtils.getAuthenticatedUsername();
        Set<String> roles = SecurityUtils.getAuthenticatedRole();

        Course course = courseRepo.findCourseByID(courseId);

        if (course == null) {
            throw new RequestFieldNotFoundException(
                    String.format("Resource Course's id {%s} not found", courseId)
            );
        }

        if (roles.contains("ROLE_ADMIN_TRAINEE") ||
                roles.contains("ROLE_SUPER_ADMIN") ||
                (roles.contains("ROLE_AUTHOR") && Objects.equals(username, course.getAuthor().getUsername())) ||
                studentCourseRepo.checkEnrolledByCourseIdAndStudentName(courseId, username)
        ) {
            EnrollCourseResponse response = new EnrollCourseResponse();
            response.setCourseId(courseId);
            response.setMessage("You already have access to this course.");
            return response;

        } else {
            AppUser user = userRepo.findByUsername(username);
            StudentCourseKey key = new StudentCourseKey();
            key.setCourseId(courseId);
            key.setStudentId(user.getID());

            StudentCourse studentCourse = new StudentCourse();

            studentCourse.setID(key);
            studentCourse.setCourse(course);
            studentCourse.setStudent(user);
            studentCourse.setEnrolledAt(new Date());
            studentCourse.setProgress(0);

            studentCourseRepo.save(studentCourse);

            EnrollCourseResponse response = new EnrollCourseResponse();
            response.setCourseId(courseId);
            response.setMessage("Enroll course successfully!!!");
            return response;
        }
    }

    @Override
    public CourseDto getCourseInfoById(Long courseId) {
        String username = SecurityUtils.getAuthenticatedUsername();
        Set<String> roles = SecurityUtils.getAuthenticatedRole();

        Course course = courseRepo.findCourseByID(courseId);

        if (course == null) {
            throw new RequestFieldNotFoundException(
                    String.format("Resource Course's id {%s} not found", courseId)
            );
        }
        CourseDto response = new CourseDto();
        response.setID(course.getID());
        response.setName(course.getName());
        response.setAuthor(course.getAuthor().getUsername());
        response.setReleaseDate(course.getReleaseDate());
        response.setDescription(course.getDescription());

        Map<CourseInfoType, List<String>> infos = course.getCourseInfos()
                .stream()
                .collect(Collectors.groupingBy(CourseInfo::getInfoType,
                        Collectors.mapping(CourseInfo::getContent, Collectors.toList())));

        response.setSkills(infos.get(CourseInfoType.SKILL));
        response.setLearns(infos.get(CourseInfoType.LEARN));

        if (username == null) {
            response.setIsAccess(false);

        } else if (roles.contains("ROLE_SUPER_ADMIN") || roles.contains("ADMIN_TRAINEE")) {
            response.setIsAccess(true);
        } else if (roles.contains("ROLE_AUTHOR") && username.equals(course.getAuthor().getUsername())) {
            response.setIsAccess(true);
        } else {
            boolean isEnrolled = studentCourseRepo.checkEnrolledByCourseIdAndStudentName(courseId, username);
            response.setIsAccess(isEnrolled);
        }

        return response;
    }

    @Override
    public List<CourseDto> getAllCourseOfUser(String username) {

        var authorWithCourses = courseRepo
                .findCoursesByAuthorName(username)
                .stream().map(courseMapper::courseToCourseDto);

        var enrollWithCourses = studentCourseRepo
                .findEnrolledCourseByStudentUsernameOrderByEnrolledAt(username)
                .stream().map(courseMapper::studentCourseToCourseDto);
        return Stream.concat(authorWithCourses, enrollWithCourses).collect(Collectors.toList());
    }

    @Override
    public CourseDto save(CourseDto courseDto) {
        String username = SecurityUtils.getAuthenticatedUsername();

        AppUser author =  userRepo.findByUsername(username);

        if (author.getStatus().equals(AccountStatus.INACTIVE)) {
            throw new AccessDeniedException("Oops, user is disable");
        }

        Category category = categoryRepo.findByName(courseDto.getCategory());

        if (category == null) {
            throw new RequestFieldNotFoundException(
                    String.format("Failed create course!!!, category %s not found", courseDto.getCategory())
            );
        }

        Course course = courseMapper.courseDtoToCourse(courseDto);
        course.setCategory(category);
        course.setAuthor(author);

        Course createdCourse = courseRepo.save(course);

        return courseMapper.courseToCourseDto(createdCourse);
    }
    @Override
    public CourseContentDto getCourseContentById(Long courseId) {
        boolean isFullAccess = validateCourseResource.checkFullAccess(courseId);

        Course course = courseRepo.findCourseByID(courseId);
        CourseContentDto result = new CourseContentDto();
        result.setID(course.getID());
        result.setName(course.getName());

        List<WeekDto> weeks = course.getWeeks().stream().map(week -> {
            WeekDto weekDto = new WeekDto();
            weekDto.setID(week.getID());
            weekDto.setName(week.getName());
            weekDto.setWeekOrder(week.getWeekOrder());

            List<WeekContentDto> weekContents;
            if (isFullAccess) {
                weekContents = weekContentRepo.findAllByWeekIdUseProjection(week.getID())
                        .stream().map(weekContent -> {
                            WeekContentDto weekContentDto = new WeekContentDto();

                            weekContentDto.setID(weekContent.getID());
                            weekContentDto.setName(weekContent.getName());
                            weekContentDto.setContentOrder(weekContent.getContentOrder());
                            weekContentDto.setContentStatus(weekContent.getContentStatus());
                            weekContentDto.setContentType(weekContent.getContentType());

                            return weekContentDto;
                        }).collect(Collectors.toList());

            } else {
                weekContents = weekContentRepo.findWeekContentByWeekIdUseProjection(week.getID())
                        .stream().map(weekContent -> {
                            WeekContentDto weekContentDto = new WeekContentDto();

                            weekContentDto.setID(weekContent.getID());
                            weekContentDto.setName(weekContent.getName());
                            weekContentDto.setContentOrder(weekContent.getContentOrder());
                            weekContentDto.setContentStatus(weekContent.getContentStatus());
                            weekContentDto.setContentType(weekContent.getContentType());

                            return weekContentDto;
                        }).collect(Collectors.toList());

            }
            weekDto.setContents(weekContents);
            return weekDto;

        }).collect(Collectors.toList());

        result.setWeeks(weeks);

        return result;

    }

    @Override
    public WeekDto saveWeek(Long courseId, WeekDto weekDto) {
        Course course = validateCourseResource.checkCourseExists(courseId);

        Week week = weekMapper.weekDtoToWeek(weekDto);
        week.setCourse(course);
        Week created = weekRepo.save(week);

        return weekMapper.weekToPlainWeekDto(created);
    }

    @Override
    public void deleteCourse(Long courseId) {
        validateCourseResource.checkCourseExists(courseId);
        courseRepo.deleteById(courseId);
    }

    @Override
    public WeekDto renameWeek(Long courseId, Long weekId, String name) {
        Week week = validateCourseResource.checkWeekExists(courseId, weekId);

        week.setName(name);

        WeekDto updatedWeek = weekMapper.weekToPlainWeekDto(week);
        updatedWeek.setCourseId(courseId);

        return updatedWeek;
    }

    @Override
    public WeekContentDto createLectureContent(
            Long courseId,
            Long weekId,
            LectureContentFormData formData
    ) throws IOException {
        Week week = validateCourseResource.checkWeekExists(courseId, weekId);

        if (!formData.getContentType().equals(CourseContentType.QUIZ)){
            Lecture lecture = new Lecture();
            lecture.setWeek(week);
            lecture.setName(formData.getName());
            lecture.setContentOrder(formData.getContentOrder());
            lecture.setContentStatus(formData.getContentStatus());
            lecture.setReleaseDate(new Date());
            lecture.setContentType(formData.getContentType());

            String key = String.format("%s_%d_%d", formData.getName(), weekId, formData.getContentOrder());
            amazonS3Service.uploadFile(key, formData.getFile());
            lecture.setContentPath(key);
            weekContentRepo.save(lecture);
            log.info("DONE CREATE CONTENT WEEK");

            return weekContentMapper.lectureToWeekContentDto(lecture, weekId);
        }
        else return null;
    }

    @Override
    public void deleteWeekContent(Long courseId, Long weekId, Long contentId) {
        WeekContent weekContent =
                validateCourseResource.checkWeekContentExists(courseId, weekId, contentId);

        if(!weekContent.getContentType().equals(CourseContentType.QUIZ)) {
            String contentPath = lectureRepo.findContentPathByLectureId(contentId);
            amazonS3Service.deleteFile(contentPath);
            weekContentRepo.deleteById(contentId);
        } else {
            weekContent.setIsActive(false);
        }
    }

    @Override
    public WeekContentDto getLectureContentUrl(Long courseId, Long weekId, Long contentId) {
        validateCourseResource.checkWeekContentExists(courseId, weekId, contentId, false);

        String contentPath = lectureRepo.findContentPathByLectureId(contentId);
        String url = amazonS3Service.createUrl(contentPath);
        WeekContentDto response = new WeekContentDto();
        response.setUrl(url);
        response.setWeekId(weekId);
        return response;
    }

    @Override
    public WeekContentDto updateLectureContent(
            Long courseId,
            Long weekId,
            Long contentId,
            LectureContentFormData formData
    ) throws IOException {
        Lecture curLecture = (Lecture) validateCourseResource.checkWeekContentExists(courseId, weekId, contentId);

        curLecture.setContentStatus(formData.getContentStatus());
        curLecture.setName(formData.getName());

        if(formData.getFile() != null) {
            log.info("MATCH IN UPDATE WITH FILE");
            String key = curLecture.getContentPath();
            amazonS3Service.uploadFile(key, formData.getFile());

        }

        return weekContentMapper.lectureToWeekContentDto(curLecture, weekId);

    }

    @Override
    public List<CourseDto> getAllCourseInSystem(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, COURSE_PAGE_SIZE, Sort.by("ID"));
        Page<Course> users = courseRepo.findAll(pageable);
        return users.stream().map(courseMapper::courseToCourseDtoForAdmin).collect(Collectors.toList());
    }
}
