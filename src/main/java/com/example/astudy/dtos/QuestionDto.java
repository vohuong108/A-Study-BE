package com.example.astudy.dtos;

import com.example.astudy.enums.QuestionType;
import lombok.Data;

import java.util.List;

@Data
public class QuestionDto {
    private Long ID;
    private String name;
    private QuestionType questionType;
    private Integer score;
    private Integer questionOrder;
    private Boolean isCorrect;
    private List<OptionDto> options;
}
