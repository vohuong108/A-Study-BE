package com.example.astudy.entities;

import com.example.astudy.enums.SubmitState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity(name = "submit")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Submit {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SubmitState state;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishTime;

    @Column(nullable = false)
    private int grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private AppUser student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @OneToMany(mappedBy = "submit", cascade = CascadeType.ALL)
    Set<Selected> selected;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_session_id")
    private QuizSession quizSession;

}
