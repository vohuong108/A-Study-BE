package astudy.controllers;

import astudy.dtos.CourseDto;
import astudy.dtos.LectureDto;
import astudy.dtos.MessageResponse;
import astudy.dtos.WeekDto;
import astudy.services.CourseService;
import astudy.utils.HandleToken;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
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

    @PostMapping(value = "/course/createlecture", consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<?> createLecture(
            @ModelAttribute LectureDto data
    ) {
        try {
            courseService.createLecture(data);
            String message = "Uploaded the file successfully: " + data.getFile().getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
        } catch (IOException e) {
            log.error("err mapper: {}", e.getMessage());
            log.error("error file: {}", data.getFile().getOriginalFilename());
            String message = "Could not upload the file: " + data.getFile().getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
        }
    }

    @GetMapping("/course/lecture/{id}")
    public ResponseEntity<ByteArrayResource> getLectureById(@PathVariable("id") Long lecId, HttpServletResponse response) throws IOException {
        LectureDto result = courseService.findLectureById(lecId);
        ByteArrayResource resource = new ByteArrayResource(result.getContent());

        return ResponseEntity
                .status(HttpStatus.PARTIAL_CONTENT)
                .header("Content-Type", "video/mp4")
                .header("Accept-Ranges", "bytes")
                .header("Content-Length", String.valueOf(result.getContent().length))
                .header("Content-Range", "bytes" + " " + 0 + "-" + 2000 + "/" + String.valueOf(result.getContent().length))
                .body(resource);
    }
}
