package astudy.models;

import astudy.enums.LectureStatus;
import astudy.enums.LectureType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "lecture")
public class Lecture {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false)
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

    @Column
    private Date releaseDate;

    @ManyToOne
    @JoinColumn(name = "week_id", nullable = false)
    private Week week;
}
