package astudy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


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

    @JsonProperty("lectures")
    private List<LectureDto> lectures;
}
