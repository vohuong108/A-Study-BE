package astudy.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QuestionChoiceResponse {
    @JsonProperty("choiceId")
    private Long choiceId;

    @JsonProperty("choice")
    private String choice;

    @JsonProperty("answer")
    private boolean answer;
}
