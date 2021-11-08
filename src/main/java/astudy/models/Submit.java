package astudy.models;

import astudy.enums.SubmitState;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "submit")
public class Submit {
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private Long ID;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SubmitState state;

    @Column(nullable = false)
    private Date startTime;

    @Column(nullable = false)
    private Date finishTime;

    @Column(nullable = false)
    private int grade;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @OneToMany(mappedBy = "submit")
    List<Choosed> choosedList;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;
}
