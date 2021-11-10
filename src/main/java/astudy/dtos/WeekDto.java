package astudy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class WeekDto {
    @JsonProperty("weekId")
    private Long weekId;

    @JsonProperty("serialWeek")
    private int serialWeek;

    @JsonProperty("name")
    private String name;

    @JsonProperty("courseId")
    private Long courseId;
}
