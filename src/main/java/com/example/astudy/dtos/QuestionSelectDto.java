package com.example.astudy.dtos;

import lombok.Data;

import java.util.List;

@Data
public class QuestionSelectDto {
    private Long questionId;
    private List<Long> selectedIds;
}
