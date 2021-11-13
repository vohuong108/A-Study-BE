package astudy.controllers;

import astudy.dtos.LectureDto;
import astudy.dtos.MessageResponse;
import astudy.dtos.WeekDto;
import astudy.response.EditQuizResponse;
import astudy.response.RenameWeekData;
import astudy.services.WeekService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin
@RequestMapping("/api")
public class WeekController {
    private final WeekService weekService;

    @PutMapping("/week/rename")
    public ResponseEntity<?> renameWeek(@RequestBody RenameWeekData renameWeekData)  {
        return ResponseEntity.ok().body( weekService.renameWeek(renameWeekData));
    }

    @PostMapping(value = "/week/createlecture", consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<?> createLecture(@ModelAttribute LectureDto data) {
        try {
            LectureDto response = weekService.createLecture(data);
//            String message = "Uploaded the file successfully: " + data.getFile().getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (IOException e) {
            log.error("err mapper: {}", e.getMessage());
            log.error("error file: {}", data.getFile().getOriginalFilename());
            String message = "Could not upload the file: " + data.getFile().getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
        }
    }

    @GetMapping("/week/lecture/{type}/{id}")
    public ResponseEntity<ByteArrayResource> getLectureVideo(
            @PathVariable("id") Long lecId,
            @PathVariable("type") String type,
            HttpServletResponse response) throws IOException {
        if (type.equals("video")) {
            byte[] result = weekService.getLectureVideoOrText(lecId);
            ByteArrayResource resource = new ByteArrayResource(result);

            return ResponseEntity
                    .status(HttpStatus.PARTIAL_CONTENT)
                    .header("Content-Type", "video/mp4")
                    .header("Accept-Ranges", "bytes")
                    .header("Content-Length", String.valueOf(result.length))
                    .header("Content-Range", "bytes" + " " + 0 + "-" + 2000 + "/" + result.length)
                    .body(resource);
        } else if (type.equals("text")) {
            byte[] result = weekService.getLectureVideoOrText(lecId);
            ByteArrayResource resource = new ByteArrayResource(result);
            return ResponseEntity
                    .status(HttpStatus.PARTIAL_CONTENT)
                    .header("Content-Type", "text/plain")
                    .header("Content-Length", String.valueOf(result.length))
                    .header("Content-Range", "bytes" + " " + 0 + "-" + 2000 + "/" + result.length)
                    .body(resource);
        } else return null;

    }

    @PostMapping(value = "/week/createquiz")
    public ResponseEntity<?> createQuiz(@RequestBody EditQuizResponse data) {
        LectureDto response = weekService.createQuiz(data);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

}
