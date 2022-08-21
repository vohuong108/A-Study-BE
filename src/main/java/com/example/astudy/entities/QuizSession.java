package com.example.astudy.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "quiz_session")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuizSession {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long sessionId;

    @Column(nullable = false)
    private Date startTime;

    @Column(nullable = false)
    private int time;

    @Column(nullable = false, columnDefinition = "BOOLEAN default 1")
    private boolean sessionActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    public QuizSession(Date startTime, int time, AppUser user, Quiz quiz) {
        this.startTime = startTime;
        this.time = time;
        this.user = user;
        this.quiz = quiz;
    }
}
