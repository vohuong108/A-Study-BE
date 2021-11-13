package astudy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class QuizDto {
    @JsonProperty("title")
    private String title;

    @JsonProperty("maxScore")
    private int maxScore;

    @JsonProperty("degree")
    private String degree;

    @JsonProperty("time")
    private int time;

    @JsonProperty("releaseDate")
    private Date releaseDate;

    @JsonProperty("closeDate")
    private Date closeDate;

    @JsonProperty("status")
    private String quizStatus;

    @JsonProperty("attemptAllow")
    private int attemptAllow;
}
