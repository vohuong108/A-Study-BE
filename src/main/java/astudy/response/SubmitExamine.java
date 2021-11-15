package astudy.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SubmitExamine {
    @JsonProperty("quizId")
    private Long quizId;

    @JsonProperty("quizTitle")
    private String quizTitle;

    @JsonProperty("startTime")
    private String startTime;

    @JsonProperty("finishTime")
    private String finishTime;

    @JsonProperty("answers")
    List<Answer> answers;
}
