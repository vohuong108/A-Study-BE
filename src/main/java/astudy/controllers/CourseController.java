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

    @GetMapping("/course/{id}")
    public ResponseEntity<?> getEditCourseById(@PathVariable("id") Long id) throws IOException {
        return ResponseEntity.ok().body( courseService.findEditCourseById(id));
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

    @DeleteMapping("/course/{id}")
    public CourseDto deleteCourseById() {
        return null;
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
