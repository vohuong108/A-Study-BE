package astudy.controllers;

import astudy.response.SubmitExamine;
import astudy.services.QuizService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin
@RequestMapping("/api")
public class QuizController {
    private final QuizService quizService;

    @GetMapping("/quiz/{id}")
    public ResponseEntity<?> getQuizById(
            @PathVariable("id") Long quizId) {
        return ResponseEntity.ok().body(quizService.getQuizById(quizId));
    }

    @PostMapping("/quiz/grade")
    public ResponseEntity<?> gradeSubmitExamine(
            @RequestBody SubmitExamine data) {
        return ResponseEntity.ok().body(quizService.handleSubmitExamine(data));
    }
}
