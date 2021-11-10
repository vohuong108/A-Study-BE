package astudy.response;


import astudy.dtos.WeekDto;
import astudy.models.Week;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class EditCourseResponse {

    @JsonProperty("courseId")
    private Long ID;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("category")
    private String category;

    @JsonProperty("weeks")
    List<WeekDto> weeks;
}
