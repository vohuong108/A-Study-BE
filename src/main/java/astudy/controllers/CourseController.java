package astudy.controllers;

import astudy.dtos.CourseDto;
import astudy.dtos.WeekDto;
import astudy.repositories.WeekRepository;
import astudy.response.DeleteCourseResponse;
import astudy.services.CourseService;
import astudy.utils.HandleToken;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.*;


@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin
@RequestMapping("/api")
public class CourseController {
    private CourseService courseService;

    @GetMapping("/course/search")
    public ResponseEntity<?> searchCourse(
            @RequestParam("q") String query) {
        return ResponseEntity.ok().body(courseService.searchCourse(query));
    }

    @GetMapping("/course/enroll/{courseId}")
    public ResponseEntity<?> enrollCourse(
            @RequestHeader("Authorization") String token,
            @PathVariable("courseId") Long courseId) {
        String username = HandleToken.getUsernameFromToken(token);
        return ResponseEntity.ok().body( courseService.enrollCourse(username, courseId));
    }

    @GetMapping("/course/overview/{id}")
    public ResponseEntity<?> getOverviewCourse(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String token) {
        if(token.equals("")) {
            return ResponseEntity.ok().body( courseService.findOverviewCourse(id, null));
        } else {
            String username = HandleToken.getUsernameFromToken(token);
            return ResponseEntity.ok().body( courseService.findOverviewCourse(id, username));
        }
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<?> getEditCourseById(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String token) throws IOException {
        String username = HandleToken.getUsernameFromToken(token);
        return ResponseEntity.ok().body( courseService.findEditCourseById(id, username));
    }

    @GetMapping("/courses/admin")
    public ResponseEntity<?> getAllCourseAdmin(@RequestHeader("Authorization") String token) {
        String username = HandleToken.getUsernameFromToken(token);
        return ResponseEntity.ok().body(courseService.findAllCourseAdmin(username));
    }

    @GetMapping("/courses")
    public ResponseEntity<?> getCourses(@RequestHeader("Authorization") String token) throws IOException {
        String username = HandleToken.getUsernameFromToken(token);
        return ResponseEntity.ok().body(courseService.findAllCourse(username));
    }

    @PostMapping("/course/createcourse")
    public ResponseEntity<?> createCourse(
            @RequestBody CourseDto body,
            @RequestHeader("Authorization") String token) {
        log.info("create course data: {}", body.toString());
        String username = HandleToken.getUsernameFromToken(token);

        return ResponseEntity.ok().body(courseService.createCourse(body, username));
    }

    @PostMapping("/course/createweek")
    public ResponseEntity<?> createWeek(@RequestBody WeekDto body) {
        return ResponseEntity.ok().body(courseService.createWeek(body));
    }

    private WeekRepository weekRepository;

    @DeleteMapping("/course/delete/{id}")
    public ResponseEntity<?> deleteCourseByID(@PathVariable("id") Long id) {
        courseService.deleteCourseById(id);
        return ResponseEntity.ok().body(
                new DeleteCourseResponse(id, "delete course successfully")
        );
    }

}
