package com.example.astudy.services.serviceImpl;

import com.example.astudy.config.security.share.ValidateCourseResource;
import com.example.astudy.config.security.utils.SecurityUtils;
import com.example.astudy.dtos.QuestionSelectDto;
import com.example.astudy.dtos.QuizDto;
import com.example.astudy.dtos.ScoringQuizDto;
import com.example.astudy.dtos.WeekContentDto;
import com.example.astudy.dtos.mapper.QuizMapper;
import com.example.astudy.dtos.mapper.WeekContentMapper;
import com.example.astudy.entities.*;
import com.example.astudy.enums.SubmitState;
import com.example.astudy.exceptions.QuizSessionException;
import com.example.astudy.repositories.*;
import com.example.astudy.services.QuizService;
import com.example.astudy.task.CheckSubmitRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class QuizServiceImpl implements QuizService {
    private static final int DELAY_ACCEPT_REQUEST = 2;
    private static final int DELAY_RUN_TASK = 3;
    private final WeekContentRepo weekContentRepo;
    private final UserRepo userRepo;
    private final QuizSessionRepo quizSessionRepo;
    private final QuizRepo quizRepo;
    private final SubmitRepo submitRepo;
    private final OptionRepo optionRepo;
    private final QuizMapper quizMapper;
    private final WeekContentMapper weekContentMapper;
    private final ValidateCourseResource validateCourseResource;
    private final ThreadPoolTaskScheduler taskScheduler;

    @Override
    public WeekContentDto saveQuiz(@NonNull Long courseId, @NonNull Long weekId, @NonNull QuizDto quizInput) {
        Week week = validateCourseResource.checkWeekExists(courseId, weekId);

        Quiz quiz = quizMapper.quizDtoToQuiz(quizInput, week);
        Quiz createdQuiz = weekContentRepo.save(quiz);
        return weekContentMapper.createdQuizToWeekContentDto(createdQuiz);
    }

    @Override
    public QuizDto getQuizContent(@NonNull Long courseId, @NonNull Long weekId, @NonNull Long quizId) {
        Quiz quiz = (Quiz) validateCourseResource.checkWeekContentExists(courseId, weekId, quizId);
        return quizMapper.quizToQuizDto(quiz, weekId);
    }

    @Override
    public QuizDto getQuizOverview(@NonNull Long courseId, @NonNull Long weekId, @NonNull Long quizId) {
        Quiz quiz = (Quiz) validateCourseResource.checkWeekContentExists(courseId, weekId, quizId);
        return quizMapper.quizToQuizDtoOverview(quiz, weekId);
    }

    @Override
    public WeekContentDto updateQuizContent(
            @NonNull Long courseId,
            @NonNull Long weekId,
            @NonNull Long quizId,
            @NonNull QuizDto updateQuiz
    ) {
        Quiz curQuiz = (Quiz) validateCourseResource.checkWeekContentExists(courseId, weekId, quizId);
        curQuiz.setIsActive(false);

        Quiz quiz = quizMapper.quizDtoToQuiz(updateQuiz, curQuiz.getWeek());
        Quiz createdQuiz = weekContentRepo.save(quiz);
        return weekContentMapper.createdQuizToWeekContentDto(createdQuiz);
    }

    @Override
    public QuizDto doQuiz(@NonNull Long quizId) {
        String username = SecurityUtils.getAuthenticatedUsername();
        AppUser user = userRepo.findByUsername(username);

        QuizSession operatingSession = quizSessionRepo.findQuizSessionOperating(quizId, user.getID());

        if (operatingSession != null) {
            log.info("MATCH IN SESSION OPERATING");

            Date startTime = operatingSession.getStartTime();
            Date nowTime = new Date();
            long operatingTime = (nowTime.getTime() - startTime.getTime())/1000;

            if (operatingTime < operatingSession.getTime()) {
                Quiz quiz = operatingSession.getQuiz();
                QuizDto response = quizMapper.quizToQuizDtoForDo(quiz, operatingSession.getSessionId());
                response.setTime((int) (quiz.getTime() - operatingTime));
                return response;
            } else {
                var scheduledTaskOfSession = SCHEDULED_TASKS.get(operatingSession.getSessionId());

                if (scheduledTaskOfSession != null && !scheduledTaskOfSession.isDone()) {
                    throw new QuizSessionException(
                            "Too many request !!! Please submit your test answers in the previous session "
                            + operatingSession.getSessionId());
                } else {
                    operatingSession.setSessionActive(false);
                }
            }

        }

        log.info("MATCH IN CREATE NEW SESSION");

        if (!checkAttemptAllowDoQuiz(quizId, user.getID())) {
            throw new QuizSessionException("Number of test executions exceeded");
        }

        Quiz quiz = (Quiz) weekContentRepo.findWeekContentByID(quizId);

        QuizSession createdQuizSession = quizSessionRepo.save(new QuizSession(new Date(), quiz.getTime(), user, quiz));

        setTaskScheduler(createdQuizSession.getSessionId(), quiz.getTime());

        return quizMapper.quizToQuizDtoForDo(quiz, createdQuizSession.getSessionId());
    }

    protected void setTaskScheduler(Long sessionId, int alarmTime) {
        log.error("SESSION IS BEING SCHEDULED TASK WITH ID {}", sessionId);
        Calendar c =  Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, alarmTime + DELAY_RUN_TASK);

        var task = new CheckSubmitRequest(sessionId, quizSessionRepo, quizRepo, userRepo, submitRepo);
        var scheduledTask = taskScheduler.schedule(task, c.getTime());
        SCHEDULED_TASKS.put(sessionId, scheduledTask);
    }

    protected boolean checkAttemptAllowDoQuiz(Long quizId, Long userId) {
        Set<String> roles = SecurityUtils.getAuthenticatedRole();

        if (
                Objects.requireNonNull(roles).contains("ROLE_SUPER_ADMIN") ||
                Objects.requireNonNull(roles).contains("ROLE_ADMIN_TRAINEE") ||
                Objects.requireNonNull(roles).contains("ROLE_AUTHOR")
        ) {
            return true;
        } else {
            log.info("MATCH IN STUDENT DOING QUIZ");
            int attemptAllow = quizRepo.findAttemptAllowByQuizId(quizId);
            int numOfExec = quizSessionRepo.countQuizSessionByQuizIdAndUserId(quizId, userId);

            return numOfExec < attemptAllow;

        }
    }

    @Override
    public ScoringQuizDto scoringQuiz(@NonNull Long quizId, @NonNull ScoringQuizDto scoringInput) {
        Date nowTime = new Date();
        QuizSession session = quizSessionRepo.findQuizSessionBySessionId(scoringInput.getSessionId());

        if (session.isSessionActive()) {
            Date startTime = session.getStartTime();
            int time = session.getTime();

            if ((nowTime.getTime() - startTime.getTime())/1000 > (time + DELAY_ACCEPT_REQUEST)) {
                ScoringQuizDto scoringQuizDto = new ScoringQuizDto();
                scoringQuizDto.setGrade(0);
                scoringQuizDto.setMessage("Your submission has expired");

                return scoringQuizDto;
            } else {
                log.error("SESSION ID {} - SCHEDULED_TASK_SIZE {}", session.getSessionId(), SCHEDULED_TASKS.size());

                var scheduledTaskOfSession = SCHEDULED_TASKS.get(session.getSessionId());
                System.out.println(SCHEDULED_TASKS.keySet().iterator().next());
                System.out.println(scheduledTaskOfSession);
                System.out.println(SCHEDULED_TASKS.get(SCHEDULED_TASKS.keySet().iterator().next()));


                if(scheduledTaskOfSession != null) {
                    log.error("NON NULL SCHEDULED TASK");
                    SCHEDULED_TASKS.remove(session.getSessionId());
                    scheduledTaskOfSession.cancel(false);

                }

                session.setSessionActive(false);

                Submit submit = new Submit();
                submit.setQuiz(session.getQuiz());
                submit.setStudent(session.getUser());
                submit.setStartTime(startTime);
                submit.setFinishTime(nowTime);
                submit.setQuizSession(session);


                Map<Long, List<Long>> SELECTED_INPUT_MAP = scoringInput.getData()
                        .stream()
                        .collect(Collectors.toMap(QuestionSelectDto::getQuestionId, QuestionSelectDto::getSelectedIds));

                List<Long> questionIds = new ArrayList<>(SELECTED_INPUT_MAP.keySet());


                List<Option> OPTIONS_DB = optionRepo.findOptionByQuestionIds(questionIds);

                if (OPTIONS_DB.size() == 0) {
                    throw new QuizSessionException("Invalid submission against our database");
                }

                Map<Long, Option> OPTION_DB_MAP = new HashMap<>();
                Map<Long, Question> QUESTION_DB_MAP = new HashMap<>();
                Map<Long, List<Long>> COR_QUESTION_OPTION_DB_MAP = new HashMap<>();


                for (Option option : OPTIONS_DB) {
                    OPTION_DB_MAP.put(option.getID(), option);

                    Question question = option.getQuestion();
                    Long questionId = question.getID();
                    QUESTION_DB_MAP.putIfAbsent(questionId, question);

                    COR_QUESTION_OPTION_DB_MAP.putIfAbsent(questionId, new ArrayList<>());

                    if (option.getIsCorrect()) {
                        COR_QUESTION_OPTION_DB_MAP.get(questionId).add(option.getID());
                    }

                }

                Set<Selected> selectedOfSubmit = new HashSet<>();

                int totalScore = 0;
                int fullScore = 0;

                for (Long questionId : questionIds) {
                    Question question = QUESTION_DB_MAP.get(questionId);
                    int score = question.getScore();
                    fullScore += score;

                    List<Long> optIn = SELECTED_INPUT_MAP.get(questionId);
                    List<Long> corOpt = COR_QUESTION_OPTION_DB_MAP.get(questionId);
                    
                    if (optIn.size() == 0) {
                        selectedOfSubmit.add(new Selected(null, submit, question, null));
                        
                    } else if (optIn.size() != corOpt.size()) {
                        int countNonNull = 0;
                        for (Long optId : optIn) {
                            Option option = OPTION_DB_MAP.get(optId);

                            if (option != null) {
                                countNonNull += 1;
                                selectedOfSubmit.add(new Selected(null, submit, question, option));
                            }
                        }
                        if (countNonNull == 0) {
                            selectedOfSubmit.add(new Selected(null, submit, question, null));
                        }

                    } else {
                        int countNonNull = 0;
                        for (Long optId : optIn) {
                            Option option = OPTION_DB_MAP.get(optId);

                            if (option != null) {
                                countNonNull += 1;
                                selectedOfSubmit.add(new Selected(null, submit, question, option));

                                if (!option.getIsCorrect()) {
                                    score = 0;
                                }
                            }
                        }
                        if (countNonNull == 0) {
                            selectedOfSubmit.add(new Selected(null, submit, question, null));
                        }

                        totalScore += score;
                    }
                }

                submit.setGrade(totalScore);
                if (totalScore >= 0.8 * fullScore) submit.setState(SubmitState.ACCEPTED);
                else submit.setState(SubmitState.FINISHED);
                submit.setSelected(selectedOfSubmit);

                submitRepo.save(submit);

                ScoringQuizDto response = new ScoringQuizDto();
                response.setGrade(totalScore);
                response.setSessionId(session.getSessionId());
                response.setMessage("Your grade is: " + totalScore + "/" + fullScore);

                return response;
            }

        } else {
            throw new QuizSessionException("The session of submission has expired or submitted", HttpStatus.UNAUTHORIZED);
        }
    }
}
