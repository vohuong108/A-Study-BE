package com.example.astudy.services;

import com.example.astudy.dtos.QuizDto;
import com.example.astudy.dtos.ScoringQuizDto;
import com.example.astudy.dtos.WeekContentDto;
import lombok.NonNull;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public interface QuizService {
    Map<Long, ScheduledFuture<?>> SCHEDULED_TASKS = new HashMap<>();
    WeekContentDto saveQuiz(@NonNull Long courseId, @NonNull Long weekId, @NonNull QuizDto quizInput);
    QuizDto getQuizContent(@NonNull Long courseId, @NonNull Long weekId, @NonNull Long quizId);
    QuizDto getQuizOverview(@NonNull Long courseId, @NonNull Long weekId, @NonNull Long quizId);
    WeekContentDto updateQuizContent(
            @NonNull Long courseId,
            @NonNull Long weekId,
            @NonNull Long quizId,
            @NonNull QuizDto updateQuiz
    );

    QuizDto doQuiz(@NonNull Long quizId);
    ScoringQuizDto scoringQuiz(@NonNull Long quizId, @NonNull ScoringQuizDto scoringInput);
}
