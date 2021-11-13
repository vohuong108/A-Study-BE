package astudy.models;

import astudy.enums.LectureStatus;
import astudy.enums.LectureType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "lecture")
@Data
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

    @Column(name = "release_date")
    private Date releaseDate;

    @OneToOne
    @JoinColumn(name = "content_id")
    private LectureContent lectureContent;

    @ManyToOne
    @JoinColumn(name = "week_id", nullable = false)
    private Week week;

}
