package com.example.astudy.api;


import com.example.astudy.config.security.utils.SecurityUtils;
import com.example.astudy.dtos.*;
import com.example.astudy.exceptions.RequestFieldNotFoundException;
import com.example.astudy.services.CourseService;
import com.example.astudy.services.QuizService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.example.astudy.constant.SecurityConstants.API_V1_COURSE_ROOT_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1_COURSE_ROOT_URL)
public class CourseResource {
    private final CourseService courseService;
    private final QuizService quizService;

    /**
     * COURSE READ OPERATOR
     *
     *
     * */

    @PreAuthorize("hasAuthority('course:read')")
    @PostMapping("/{id}/enroll")
    public ResponseEntity<?> enrollCourse(@PathVariable Long id) {
        return ResponseEntity
                .ok()
                .body(courseService.enrollCourse(id));
    }

    @PreAuthorize("hasAuthority('course:read')")
    @GetMapping("/user/courses")
    public ResponseEntity<List<CourseDto>> getAllCourseOfUser() {
        String username = SecurityUtils.getAuthenticatedUsername();

        return ResponseEntity
                .ok()
                .body(courseService.getAllCourseOfUser(username));
    }


    @PreAuthorize("hasAuthority('course:read')")
    @GetMapping("/{id}")
    public ResponseEntity<CourseContentDto> getCourseContentById(@NonNull @PathVariable Long id) {
        return ResponseEntity
                .ok()
                .body(courseService.getCourseContentById(id));
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN_TRAINEE', 'SUPER_ADMIN') || " +
            "@securityService.isAuthorOfCourse(#id) || " +
            "@securityService.isEnrolledAndPublicContent(#id, #contentId)"
    )
    @GetMapping(value = "/{id}/week/{weekId}/lecture/{contentId}")
    public ResponseEntity<?> getLectureContentUrl(
            @NonNull @PathVariable Long id,
            @NonNull @PathVariable Long weekId,
            @NonNull @PathVariable Long contentId) {

        return ResponseEntity
                .ok()
                .body(courseService.getLectureContentUrl(id, weekId, contentId));
    }



    @PreAuthorize(
            "hasAnyRole('ADMIN_TRAINEE', 'SUPER_ADMIN') || @securityService.isAuthorOfCourse(#id)"
    )
    @GetMapping(
            path = "/{id}/week/{weekId}/quiz/{quizId}"
    )
    public ResponseEntity<?> getQuizContentToEdit(
            @NonNull @PathVariable Long id,
            @NonNull @PathVariable Long weekId,
            @NonNull @PathVariable Long quizId
    ) {
        return ResponseEntity.ok().body(quizService.getQuizContentToEdit(id, weekId, quizId));
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN_TRAINEE', 'SUPER_ADMIN') || " +
            "@securityService.isAuthorOfCourse(#id) || " +
            "@securityService.isEnrolledAndPublicContent(#id, #quizId)"
    )
    @GetMapping(
            path = "/{id}/week/{weekId}/quiz/{quizId}/overview"
    )
    public ResponseEntity<?> getQuizOverview(
            @NonNull @PathVariable Long id,
            @NonNull @PathVariable Long weekId,
            @NonNull @PathVariable Long quizId
    ) {
        return ResponseEntity.ok().body(quizService.getQuizOverview(id, weekId, quizId));
    }



    /**
     * COURSE WRITE OPERATOR
     *
     *
     * */

    @PreAuthorize("hasAuthority('course:write')")
    @PostMapping("/save")
    public ResponseEntity<CourseDto> saveCourse(
            HttpServletRequest request,
            @NonNull @RequestBody CourseDto courseInput
    ) throws URISyntaxException {

        List<String> skills = courseInput.getSkills();
        List<String> learns = courseInput.getSkills();

        if (skills == null || skills.isEmpty()
                || learns == null || learns.isEmpty()) {
            throw new RequestFieldNotFoundException("Save course failed!!! skills and learns must be not null");
        }

        return ResponseEntity
                .created(new URI(request.getRequestURI()))
                .body(courseService.save(courseInput));
    }


    @PreAuthorize("hasRole('SUPER_ADMIN') || @securityService.isAuthorOfCourse(#id)")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteCourse(@NonNull @PathVariable Long id) {
        courseService.deleteCourse(id);

        return ResponseEntity
                .ok()
                .body("Delete this course successfully");
    }


    @PreAuthorize("hasRole('SUPER_ADMIN') || @securityService.isAuthorOfCourse(#id)")
    @PostMapping("/{id}/week/save")
    public ResponseEntity<?> saveWeek(
            HttpServletRequest request,
            @NonNull @PathVariable Long id,
            @NonNull @RequestBody WeekDto weekInput) throws URISyntaxException {
        return ResponseEntity
                .created(new URI(request.getRequestURI()))
                .body(courseService.saveWeek(id, weekInput));
    }


    @PreAuthorize("hasRole('SUPER_ADMIN') || @securityService.isAuthorOfCourse(#id)")
    @PutMapping("/{id}/week/{weekId}/rename")
    public ResponseEntity<?> renameWeek(
            @NonNull @PathVariable Long id,
            @NonNull @PathVariable Long weekId,
            @NonNull @RequestBody WeekDto weekDto
    ) {

        return ResponseEntity
                .ok()
                .body(courseService.renameWeek(id, weekId, weekDto.getName()));
    }


    //TODO: validate
    @PreAuthorize("hasRole('SUPER_ADMIN') || @securityService.isAuthorOfCourse(#id)")
    @PostMapping(
            path ="/{id}/week/{weekId}/lecture/save",
            consumes = { MediaType.MULTIPART_FORM_DATA_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<?> saveLectureContent(
            HttpServletRequest request,
            @NonNull @PathVariable Long id,
            @NonNull @PathVariable Long weekId,
            @NonNull @ModelAttribute LectureContentFormData formData) throws IOException, URISyntaxException {

        return ResponseEntity
                .created(new URI(request.getRequestURI()))
                .body(courseService.createLectureContent(id, weekId, formData));
    }


    @PreAuthorize("hasRole('SUPER_ADMIN') || @securityService.isAuthorOfCourse(#id)")
    @DeleteMapping (
            path ="/{id}/week/{weekId}/content/{contentId}/delete"
    )
    public ResponseEntity<?> deleteWeekContent(
            @NonNull @PathVariable Long id,
            @NonNull @PathVariable Long weekId,
            @NonNull @PathVariable Long contentId) {

        courseService.deleteWeekContent(id, weekId, contentId);
        return ResponseEntity
                .ok()
                .body("Delete week content successful!!!");
    }


    //TODO: expired pre-signed url
    @PreAuthorize("hasRole('SUPER_ADMIN') || @securityService.isAuthorOfCourse(#id)")
    @PutMapping(
            path = "/{id}/week/{weekId}/lecture/{contentId}/update",
            consumes = { MediaType.MULTIPART_FORM_DATA_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<?> updateLectureContent(
            @NonNull @PathVariable Long id,
            @NonNull @PathVariable Long weekId,
            @NonNull @PathVariable Long contentId,
            @NonNull @ModelAttribute LectureContentFormData formData) throws IOException {

        return ResponseEntity
                .ok()
                .body(courseService.updateLectureContent(id, weekId, contentId, formData));
    }


    @PreAuthorize("hasRole('SUPER_ADMIN') || @securityService.isAuthorOfCourse(#id)")
    @PostMapping(
            path = "/{id}/week/{weekId}/quiz/save"
    )
    public ResponseEntity<?> saveQuiz(
            HttpServletRequest request,
            @NonNull @PathVariable Long id,
            @NonNull @PathVariable Long weekId,
            @RequestBody QuizDto quizInput
    ) throws URISyntaxException {
        return ResponseEntity
                .created(new URI(request.getRequestURI()))
                .body(quizService.saveQuiz(id, weekId, quizInput));
    }


    @PreAuthorize("hasRole('SUPER_ADMIN') || @securityService.isAuthorOfCourse(#id)")
    @PutMapping(
            path = "/{id}/week/{weekId}/quiz/{quizId}/update"
    )
    public ResponseEntity<?> updateQuizContent(
            @NonNull @PathVariable Long id,
            @NonNull @PathVariable Long weekId,
            @NonNull @PathVariable Long quizId,
            @NonNull @RequestBody QuizDto quizInput
    ) {
        return ResponseEntity.ok().body(quizService.updateQuizContent(id, weekId, quizId, quizInput));
    }
}
