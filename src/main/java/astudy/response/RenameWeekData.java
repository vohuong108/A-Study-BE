package astudy.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RenameWeekData {
    @JsonProperty("courseId")
    private Long courseId;

    @JsonProperty("serialWeek")
    private int serialWeek;

    @JsonProperty("newName")
    private String newName;

    @JsonProperty("weekId")
    private Long weekId;

}
