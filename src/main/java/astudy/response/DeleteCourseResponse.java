package astudy.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeleteCourseResponse {
    public DeleteCourseResponse(Long courseId, String message) {
        this.courseId = courseId;
        this.message = message;
    }

    @JsonProperty("courseId")
    private Long courseId;

    @JsonProperty("message")
    private String message;
}
