package astudy.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class EditQuizResponse {
    @JsonProperty("quizId")
    private Long quizId;

    @JsonProperty("weekId")
    private Long weekId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("maxScore")
    private int maxScore;

    @JsonProperty("degree")
    private String degree;

    @JsonProperty("time")
    private int time;

    @JsonProperty("releaseDate")
    private String releaseDate;

    @JsonProperty("dueDate")
    private String closeDate;

    @JsonProperty("status")
    private String status;

    @JsonProperty("attemptAllow")
    private int attemptAllow;

    @JsonProperty("indexLecture")
    private int indexLecture;

    @JsonProperty("questions")
    List<QuizQuestionResponse> questions;
}
