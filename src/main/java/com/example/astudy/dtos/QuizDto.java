package com.example.astudy.dtos;

import com.example.astudy.enums.Degree;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class QuizDto extends WeekContentDto {
    private Integer maxScore;
    private Degree degree;
    private Integer time;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            timezone = "UTC"
    )
    private Date closeDate;
    private Integer attemptAllow;
    private List<QuestionDto> questions;

    private Long sessionId;
}
