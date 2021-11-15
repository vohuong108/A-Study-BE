package astudy.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QuizResult {
    @JsonProperty("quizId")
    private Long quizId;

    @JsonProperty("quizTitle")
    private String quizTitle;

    @JsonProperty("score")
    private String score;
}
