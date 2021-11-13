package astudy.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "lecture_content")
@Data
public class LectureContent {
    public LectureContent() {
        super();
    }

    public LectureContent(byte[] content) {
        super();
        this.content = content;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long ID;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(length = Integer.MAX_VALUE)
    private byte[] content;

    @OneToOne(mappedBy = "lectureContent", cascade = CascadeType.ALL)
    private Lecture lecture;
}
