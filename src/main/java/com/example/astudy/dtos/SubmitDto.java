package com.example.astudy.dtos;

import com.example.astudy.enums.SubmitState;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SubmitDto {
    private Long ID;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            timezone = "UTC"
    )
    private Date finishTime;
    private Date startTime;
    private int grade;
    private SubmitState state;
    private List<QuestionDto> questions;
}
