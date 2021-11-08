package astudy.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "course")
public class Course {

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private Long ID;

    @Getter
    @Setter
    @Column(nullable = false, columnDefinition = "TEXT")
    private String name;

    @Getter
    @Setter
    @Column(columnDefinition = "TEXT")
    private String description;

    @Getter
    @Setter
    @Column(columnDefinition = "TEXT", nullable = false)
    private String learnInfo;

    @Getter
    @Setter
    @Column(columnDefinition = "TEXT", nullable = false)
    private String skillInfo;


    @Getter
    @Setter
    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date releaseDate;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Getter
    @Setter
    @OneToMany(mappedBy = "course")
    Set<courseStudent> courseStudents;

    @Getter
    @Setter
    @OneToMany(mappedBy = "course")
    List<Week> weeks;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
