package astudy.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "course")
@Data
public class Course {

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private Long ID;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT", nullable = false, name = "learn_info")
    private String learnInfo;

    @Column(columnDefinition = "TEXT", nullable = false, name = "skill_info")
    private String skillInfo;

    @Column(nullable = false, name = "release_date")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date releaseDate;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToMany(mappedBy = "course")
    List<CourseStudent> courseStudents;

    @OneToMany(mappedBy = "course")
    List<Week> weeks;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
