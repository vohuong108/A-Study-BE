package astudy.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeleteLectureQuizResponse {
    public DeleteLectureQuizResponse(
            Long weekId, Long lectureId, String message
    ) {
        this.weekId = weekId;
        this.lectureId = lectureId;
        this.message = message;
    }
    @JsonProperty("weekId")
    private Long weekId;

    @JsonProperty("lectureId")
    private Long lectureId;

    @JsonProperty("message")
    private String message;
}
