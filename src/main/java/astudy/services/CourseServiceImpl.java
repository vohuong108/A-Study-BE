package astudy.services;

import astudy.dtos.CourseDto;
import astudy.dtos.LectureDto;
import astudy.dtos.WeekDto;
import astudy.enums.Permission;
import astudy.models.*;
import astudy.repositories.*;
import astudy.response.AllCourseAdmin;
import astudy.response.EditCourseResponse;
import astudy.response.OverviewCourse;
import astudy.response.SearchCourseResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService{
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final WeekRepository weekRepository;
    private final WeekService weekService;
    private final CourseStudentRepository courseStudentRepository;
    private final RoleRepository roleRepository;

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
        Long roleId = userRepository.findRoleIdByUsername(username);
        String roleName = roleRepository.findRoleByID(roleId);

        if (roleName.equals("ADMIN")) {
            log.info("match ADMIN role");
            //SELECT c.ID, c.name, u.username, ct.name, c.release_date
            String[][] allCourse = courseRepository.getAllCourseByAdmin();
            log.info("row length: {}", allCourse.length);

            List<CourseDto> courses = new ArrayList<>();

            for (String[] row : allCourse) {
                CourseDto ACA = new CourseDto();
                ACA.setCourseId(Long.parseLong(row[0]));
                ACA.setName(row[1]);
                ACA.setAuthor(row[2]);
                ACA.setPermissionCourse("ADMIN");
                courses.add(ACA);
            }
            return courses;

        } else {
            log.info("match student or author");
            User userDb = userRepository.findUserByUsername(username);
            Permission role = userDb.getRole().getName();

            List<CourseDto> courses = userDb.getCourseStudents().stream().map(sc -> {
                Course course = sc.getCourse();

                CourseDto temp = new CourseDto();
                temp.setCourseId(course.getID());
                temp.setName(course.getName());
                temp.setAuthor(course.getAuthor().getUsername());
                temp.setPermissionCourse("STUDENT");
                return temp;

            }).collect(Collectors.toList());

            if (role == Permission.AUTHOR) {
                //TODO: current user is author
                log.info("match author");
                List<CourseDto> courses2 = userDb.getCourses().stream().map(c -> {
                    CourseDto temp2 = new CourseDto();
                    temp2.setCourseId(c.getID());
                    temp2.setName(c.getName());
                    temp2.setAuthor(c.getAuthor().getUsername());
                    temp2.setPermissionCourse("AUTHOR");
                    return temp2;

                }).collect(Collectors.toList());
                courses.addAll(courses2);
            }

            return courses;
        }
    }

    @Override
    public List<AllCourseAdmin> findAllCourseAdmin(String username) {
        Long roleId = userRepository.findRoleIdByUsername(username);
        String role = roleRepository.findRoleByID(roleId);
        log.info("role name: {}", role);

        if (role.equals("ADMIN")) {
            log.info("match ADMIN role");
            //SELECT c.ID, c.name, u.username, ct.name, c.release_date
            String[][] allCourse = courseRepository.getAllCourseByAdmin();
            log.info("row length: {}", allCourse.length);
            List<AllCourseAdmin> courses = new ArrayList<>();

            for (String[] row : allCourse) {
                AllCourseAdmin ACA = new AllCourseAdmin();
                ACA.setCourseId(Long.parseLong(row[0]));
                ACA.setName(row[1]);
                ACA.setAuthor(row[2]);
                ACA.setCategory(row[3]);
                ACA.setReleaseDate(row[4]);
                courses.add(ACA);
            }

            return courses;

        }
        else return null;
    }

    @Override
    public EditCourseResponse findEditCourseById(Long courseId, String username) {
        Course courseDb = courseRepository.findCourseById(courseId);
//        log.info("end get edit: {}", courseDb.toString());

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
        String authorName = courseDb.getAuthor().getUsername();

        EditCourseResponse response = new EditCourseResponse();
        response.setID(courseDb.getID());
        response.setName(courseDb.getName());
        response.setDescription(courseDb.getDescription());
        response.setCategory(courseDb.getCategory().getName());
        response.setWeeks(weeks);
        if (authorName.equals(username)) response.setPermissionCourse("AUTHOR");
        else response.setPermissionCourse("STUDENT");
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

    @Override
    public OverviewCourse findOverviewCourse(Long courseId, String username) {
        log.info("username input: {}", username);
        String[][] courseDb = courseRepository.customFindCourseById(courseId);

        //SELECT ID, description, learn_info, name, release_date, skill_info, author_id
        log.info("length row: {}", courseDb.length);
        log.info("length col: {}", courseDb[0].length);

        Long authorId = Long.parseLong(courseDb[0][6]);
        String author = userRepository.findUsernameById(authorId);
        log.info("author {}", author);
        String description = courseDb[0][1];
        log.info("description {}", description);
        List<String> learns = Arrays.asList(courseDb[0][2].split(" \\| ").clone());
        log.info("learns {}", learns);
        String name = courseDb[0][3];
        log.info("name {}", name);
        List<String> skills = Arrays.asList(courseDb[0][5].split(" \\| ").clone());
        log.info("skills {}", skills);

        OverviewCourse response = new OverviewCourse();
        response.setAuthor(author);
        response.setCourseId(courseId);
        response.setDescription(description);
        response.setName(name);
        response.setLearnInfo(learns);
        response.setSkillInfo(skills);

        if (username != null) {
            if(author.equals(username)) {
                response.setEnroll(true);
            } else {
                Long userId = userRepository.findUserIdByUsername(username);
                log.info("userId: {}", userId);

                Integer isEnroll = courseStudentRepository.checkEnrolledStudent(userId, courseId);
                if (isEnroll == 1) response.setEnroll(true);
                else response.setEnroll(false);

                log.info("userid {} - isEnrolled {}", userId, isEnroll);
            }

        } else {
            response.setEnroll(false);
            log.info("username match null");
        }

        return response;
    }

    @Override
    public OverviewCourse enrollCourse(String username, Long courseId) {
        User userDb = userRepository.findUserByUsername(username);
        Course courseDb = courseRepository.findCourseById(courseId);
        log.info("userId: {}", userDb.getID());
        log.info("course name: {}", courseDb.getName());

        CourseStudent cs = new CourseStudent();
        cs.setStudent(userDb);
        cs.setCourse(courseDb);
        cs.setEnrolledAt(new Date());
        courseStudentRepository.save(cs);

        return this.findOverviewCourse(courseId, username);
    }

    @Override
    public List<SearchCourseResponse> searchCourse(String query) {
        String[][] result = courseRepository.searchCourseByQueryName("web");
        log.info("length row: {}", result.length);
        log.info("length col: {}", result[0].length);

        //ID, name, author_id
        List<SearchCourseResponse> response = new ArrayList<>();
        for (String[] row : result) {
            SearchCourseResponse temp = new SearchCourseResponse();
            String author = userRepository.findUsernameById(Long.parseLong(row[2]));

            temp.setCourseId(Long.parseLong(row[0]));
            temp.setName(row[1]);
            temp.setAuthor(author);

            log.info("ID: {} - name: {} - author: {}", Long.parseLong(row[0]), row[1], author);
            response.add(temp);
        }
        return response;
    }
}
