package astudy.models;

import astudy.enums.LectureStatus;
import astudy.enums.LectureType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "lecture")
@Getter
@Setter
public class Lecture {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false, name = "index_lecture")
    private int indexLecture;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String title;

    @Column(nullable = false, name = "type")
    @Enumerated(EnumType.STRING)
    private LectureType lectureType;

    @Column(nullable = false, name = "status")
    @Enumerated(EnumType.STRING)
    private LectureStatus lectureStatus;

    @Lob
    @Column(length = Integer.MAX_VALUE)
    private byte[] content;

    @Column(name = "release_date")
    private Date releaseDate;

    @ManyToOne
    @JoinColumn(name = "week_id", nullable = false)
    private Week week;

}
