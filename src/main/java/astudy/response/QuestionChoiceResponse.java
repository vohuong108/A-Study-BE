package astudy.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QuestionChoiceResponse {
    @JsonProperty("choiceId")
    private Long choiceId;

    @JsonProperty
    private String choice;

    @JsonProperty
    private boolean answer;
}
