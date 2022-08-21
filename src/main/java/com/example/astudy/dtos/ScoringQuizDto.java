package com.example.astudy.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ScoringQuizDto {
    private Long sessionId;
    private Integer grade;
    private String message;
    private List<QuestionSelectDto> data;
}
