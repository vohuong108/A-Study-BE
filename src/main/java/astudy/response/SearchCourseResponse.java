package astudy.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SearchCourseResponse {
    @JsonProperty("courseId")
    private Long courseId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("author")
    private String author;
}
