package astudy.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class QuizQuestionResponse {
    @JsonProperty("questionId")
    private Long questionId;

    @JsonProperty("question")
    private String question;

    @JsonProperty("point")
    private int point;

    @JsonProperty("choices")
    List<QuestionChoiceResponse> choices;
}
