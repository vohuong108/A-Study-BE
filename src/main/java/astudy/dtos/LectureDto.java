package astudy.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LectureDto {
    @JsonProperty("lectureId")
    private Long lectureId;

    @JsonProperty("indexLecture")
    private int indexLecture;

    @JsonProperty("title")
    private String title;

    @JsonProperty("lectureType")
    private String lectureType;

    @JsonProperty("lectureStatus")
    private String lectureStatus;

    @JsonProperty("weekId")
    private Long weekId;

    @JsonProperty("file")
    private MultipartFile file;

    @JsonProperty("url")
    private String url;

    @JsonProperty("content")
    private byte[] content;
}
