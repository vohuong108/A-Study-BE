package astudy.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AllCourseAdmin {
    @JsonProperty("courseId")
    private Long courseId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("category")
    private String category;

    @JsonProperty("author")
    private String author;

    @JsonProperty("releaseDate")
    private String releaseDate;
}
