package astudy.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OverviewCourse {
    @JsonProperty("courseId")
    private Long courseId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("learnInfo")
    private List<String> learnInfo;

    @JsonProperty("skillInfo")
    private List<String> skillInfo;

    @JsonProperty("author")
    private String author;

    @JsonProperty("isEnroll")
    private boolean isEnroll;
}
