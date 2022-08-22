package com.example.astudy.api;

import com.example.astudy.dtos.AdminActionUserDto;
import com.example.astudy.services.CourseService;
import com.example.astudy.services.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.astudy.constant.SecurityConstants.API_V1_ADMIN_ROOT_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1_ADMIN_ROOT_URL)
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AdminResource {
    private final CourseService courseService;
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUserInSystem(
            @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        return ResponseEntity
                .ok()
                .body(userService.getAllUserInSystem(page));
    }

    @GetMapping("/courses")
    public ResponseEntity<?> getAllCourseInSystem(
            @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        return ResponseEntity
                .ok()
                .body(courseService.getAllCourseInSystem(page));
    }

    @PutMapping("/user/changestatus")
    public ResponseEntity<?> changeStatusOfUser(@NonNull @RequestBody AdminActionUserDto input) {
        return ResponseEntity
                .ok()
                .body(userService.changeStatusOfUser(input));
    }

    @PutMapping("/user/changerole")
    public ResponseEntity<?> changeRoleOfUser(@NonNull @RequestBody AdminActionUserDto input) {
        return ResponseEntity
                .ok()
                .body(userService.changeRoleOfUser(input));
    }

    @DeleteMapping("/course/{id}/delete")
    public ResponseEntity<?> deleteCourse(@NonNull @PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity
                .ok()
                .body("Delete course successfully");
    }
}
