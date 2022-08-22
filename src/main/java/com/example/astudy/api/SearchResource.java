package com.example.astudy.api;

import com.example.astudy.services.CourseService;
import com.example.astudy.services.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.astudy.constant.SecurityConstants.API_V1_SEARCH_ROOT_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1_SEARCH_ROOT_URL)
public class SearchResource {
    private final CourseService courseService;
    private final SearchService searchService;

    @GetMapping("/course/{id}")
    public ResponseEntity<?> searchCourseInfoById(@PathVariable Long id) {
        return ResponseEntity
                .ok()
                .body(courseService.getCourseInfoById(id));
    }

    @GetMapping("")
    public ResponseEntity<?> searchCourse(@RequestParam("q") String query) {
        return ResponseEntity
                .ok()
                .body(searchService.searchCourse(query));
    }
}
