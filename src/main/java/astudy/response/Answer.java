package astudy.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Answer {
    @JsonProperty("questionId")
    private Long questionId;

    @JsonProperty("choiceId")
    private Long choiceId;
}
