package com.example.astudy.task;

import com.example.astudy.entities.*;
import com.example.astudy.enums.SubmitState;
import com.example.astudy.repositories.QuizRepo;
import com.example.astudy.repositories.QuizSessionRepo;
import com.example.astudy.repositories.SubmitRepo;
import com.example.astudy.repositories.UserRepo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.astudy.services.QuizService.SCHEDULED_TASKS;

@Slf4j
@Getter
@Setter
public class CheckSubmitRequest implements Runnable {
    private Long sessionId;
    private QuizSessionRepo quizSessionRepo;
    private QuizRepo quizRepo;
    private UserRepo userRepo;
    private SubmitRepo submitRepo;

    public CheckSubmitRequest(Long sessionId, QuizSessionRepo quizSessionRepo,
                              QuizRepo quizRepo, UserRepo userRepo, SubmitRepo submitRepo) {
        this.sessionId = sessionId;
        this.quizSessionRepo = quizSessionRepo;
        this.quizRepo = quizRepo;
        this.userRepo = userRepo;
        this.submitRepo = submitRepo;
    }

    @Override
    @Transactional
    public void run() {
        log.info("RUN TASK - " + new Date());

        if(SCHEDULED_TASKS.get(sessionId) == null) return;

        QuizSession session = quizSessionRepo.findQuizSessionActiveBySessionId(sessionId);

        if (session != null) {
            log.info("FOUND SESSION {} ACTIVE - SCHEDULED_TASK_SIZE {}", sessionId, SCHEDULED_TASKS.size());

            Submit submit = new Submit();
            submit.setQuiz(session.getQuiz());
            submit.setStudent(session.getUser());
            submit.setStartTime(session.getStartTime());
            submit.setGrade(0);
            submit.setFinishTime(new Date());
            submit.setState(SubmitState.FINISHED);
            submit.setQuizSession(session);

            Set<Selected> selectedOfSubmit = session.getQuiz().getQuestions()
                    .stream()
                    .map(x -> new Selected(null, submit, x, null))
                    .collect(Collectors.toSet());

            submit.setSelected(selectedOfSubmit);

            session.setSessionActive(false);
            quizSessionRepo.save(session);
            submitRepo.save(submit);

            SCHEDULED_TASKS.remove(sessionId);
            log.info("SESSION {} DONE - SCHEDULED_TASK_SIZE {}", sessionId, SCHEDULED_TASKS.size());
        }
    }
}
