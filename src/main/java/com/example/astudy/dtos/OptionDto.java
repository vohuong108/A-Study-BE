package com.example.astudy.dtos;

import lombok.Data;

@Data
public class OptionDto {
    private Long ID;
    private String content;
    private Boolean isCorrect;
    private Boolean isSelect;
    private Integer optionOrder;
}
