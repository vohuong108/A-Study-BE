package astudy.controllers;

import astudy.dtos.CourseDto;
import astudy.services.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@AllArgsConstructor
public class CourseController {
    private CourseService courseService;

    @GetMapping("/course/{id}")
    public ResponseEntity<?> getCourseById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return null;
    }

    @PostMapping("/course")
    public ResponseEntity<?> createCourse(@RequestBody CourseDto body) {
        return ResponseEntity.ok().body(courseService.createCourse(body));
    }

    @DeleteMapping("/course/{id}")
    public CourseDto deleteCourseById() {
        return null;
    }
}
