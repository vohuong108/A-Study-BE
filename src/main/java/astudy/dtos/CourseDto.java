package astudy.dtos;

import astudy.models.courseStudent;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    @JsonProperty("courseId")
    private Long courseId;

    @JsonProperty("courseName")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("learnInfo")
    private List<String> learnInfo;

    @JsonProperty("skillInfo")
    private List<String> skillInfo;

    @JsonProperty("category")
    private String category;

    @JsonProperty("username")
    private String username;

    private String author;

    private Date releaseDate;

}
