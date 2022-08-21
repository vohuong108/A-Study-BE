package com.example.astudy.api;

import com.example.astudy.dtos.QuizDto;
import com.example.astudy.dtos.ScoringQuizDto;
import com.example.astudy.services.QuizService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.astudy.constant.SecurityConstants.API_V1_QUIZ_ROOT_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1_QUIZ_ROOT_URL)
public class QuizResource {
    private final QuizService quizService;

    @PreAuthorize(
            "hasAnyRole('ADMIN_TRAINEE', 'SUPER_ADMIN') || @securityService.isAccessQuiz(#quizId)"
    )
    @GetMapping(
            path = "/{quizId}/do"
    )
    public ResponseEntity<?> doQuiz(
            @NonNull @PathVariable Long quizId
    ) {
        return ResponseEntity.ok().body(quizService.doQuiz(quizId));
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN_TRAINEE', 'SUPER_ADMIN') || @securityService.isAccessQuiz(#quizId)"
    )
    @PostMapping(
            path = "/{quizId}/scoring"
    )
    public ResponseEntity<?> scoringQuiz(
            @NonNull @PathVariable Long quizId,
            @NonNull @RequestBody ScoringQuizDto scoringInput
    ) {
        return ResponseEntity.ok().body(quizService.scoringQuiz(quizId, scoringInput));
    }
}
