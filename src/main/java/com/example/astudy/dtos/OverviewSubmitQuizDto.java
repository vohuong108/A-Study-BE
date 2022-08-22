package com.example.astudy.dtos;

import com.example.astudy.enums.Degree;
import lombok.Data;

import java.util.List;

@Data
public class OverviewSubmitQuizDto {
    private Long quizId;
    private String quizName;
    private Degree degree;
    private int maxScore;
    private int numOfSub;
    private List<SubmitDto> submits;

}
