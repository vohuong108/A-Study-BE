package astudy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseDto {
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

    @JsonProperty("category")
    private String category;

    @JsonProperty("username")
    private String username;

    @JsonProperty("author")
    private String author;

    @JsonProperty("permissionCourse")
    private String permissionCourse;

    @JsonProperty("releaseDate")
    private Date releaseDate;

}
