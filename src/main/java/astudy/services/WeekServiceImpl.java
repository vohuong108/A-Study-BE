package astudy.services;

import astudy.dtos.LectureDto;
import astudy.dtos.QuizDto;
import astudy.enums.Degree;
import astudy.enums.LectureStatus;
import astudy.enums.LectureType;
import astudy.models.*;
import astudy.repositories.*;
import astudy.response.EditQuizResponse;
import astudy.response.QuestionChoiceResponse;
import astudy.response.QuizQuestionResponse;
import astudy.response.RenameWeekData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class WeekServiceImpl implements WeekService {
    private final WeekRepository weekRepository;
    private final CourseRepository courseRepository;
    private final LectureRepository lectureRepository;
    private final LectureContentRepository contentRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;

    @Override
    public RenameWeekData renameWeek(RenameWeekData renameWeekData) {
        log.info("rename data: {}", renameWeekData.toString());

        Course courseDb = courseRepository.getById(renameWeekData.getCourseId());
        List<Week> listWeek = courseDb.getWeeks();

        if(listWeek.size() != 0) {

            List<Week> listWeekFilter = listWeek.stream().filter(week -> {
                if (week.getSerialWeek() == renameWeekData.getSerialWeek()) {
                    return true;
                }
                return false;
            }).collect(Collectors.toList());
            log.info("filter week size in rename: {}", listWeekFilter.size());

            if (listWeekFilter.size() > 0) {
                Week weekDb = listWeekFilter.get(0);
                weekDb.setName(renameWeekData.getNewName());
                Week result = weekRepository.save(weekDb);
                renameWeekData.setWeekId(result.getID());

                return renameWeekData;
            }

        }

        Week newWeek = new Week();
        newWeek.setSerialWeek(renameWeekData.getSerialWeek());
        newWeek.setName(renameWeekData.getNewName());
        newWeek.setCourse(courseDb);

        Week result = weekRepository.save(newWeek);
        renameWeekData.setWeekId(result.getID());

        return renameWeekData;
    }

    @Override
    public LectureDto createLecture(LectureDto newLecture) throws IOException {
        Week weekDb = weekRepository.getById(newLecture.getWeekId());

        log.info("getLectureStatus: {}", newLecture.getLectureStatus());
        log.info("getIndexLecture: {}", newLecture.getIndexLecture());
        log.info("getLectureType: {}", newLecture.getLectureType());
        log.info("getWeekId: {}", newLecture.getWeekId());
        log.info("getTitle: {}", newLecture.getTitle());
        log.info("getOriginalFilename: {}", newLecture.getFile().getOriginalFilename());

        LectureContent lectureContent = contentRepository.save(
                new LectureContent(newLecture.getFile().getBytes()));

        Lecture createLec = new Lecture();
        createLec.setIndexLecture(newLecture.getIndexLecture());
        createLec.setTitle(newLecture.getTitle());
        createLec.setWeek(weekDb);
        createLec.setReleaseDate(new Date());
        createLec.setLectureContent(lectureContent);

        String statusNewLec = newLecture.getLectureStatus().toUpperCase();

        if(LectureStatus.PRIVATE.toString().equals(statusNewLec)) {
            createLec.setLectureStatus(LectureStatus.PRIVATE);
        } else {
            createLec.setLectureStatus(LectureStatus.PUBLIC);
        }

        String NewLecType = newLecture.getLectureType().toUpperCase();

        if(LectureType.TEXT.toString().equals(NewLecType)) {
            createLec.setLectureType(LectureType.TEXT);
        } else {
            createLec.setLectureType(LectureType.VIDEO);
        }

        Lecture result = lectureRepository.save(createLec);

        LectureDto response = new LectureDto();

        response.setIndexLecture(result.getIndexLecture());
        response.setLectureStatus(result.getLectureStatus().toString());
        response.setLectureType(result.getLectureType().toString());
        response.setTitle(result.getTitle());
        response.setLectureId(result.getID());
        response.setWeekId(newLecture.getWeekId());

        return response;

    }

    @Override
    public byte[] getLectureVideoOrText(Long lectureId) {
        Lecture lecDb = lectureRepository.findByLectureId(lectureId);
        return lecDb.getLectureContent().getContent();
    }

    @Override
    public LectureDto createQuiz(EditQuizResponse quizData) {
        log.info("request data: {}", quizData.toString());
//        Date date = new Date("Sat, 20 Nov 2021 09:30:59 GMT");
//        log.info("date: {}", String.valueOf(date.toString()));
        Week weekDb = weekRepository.getById(quizData.getWeekId());

        //NOTE: create new quiz
        Quiz newQuiz = new Quiz();
        newQuiz.setAttemptAllow(quizData.getAttemptAllow());
        newQuiz.setCloseDate(new Date(quizData.getCloseDate()));

        if(quizData.getDegree().equals("EASY")) {
            newQuiz.setDegree(Degree.EASY);
        } else if(quizData.getDegree().equals("MEDIUM")) {
            newQuiz.setDegree(Degree.MEDIUM);
        } else newQuiz.setDegree(Degree.HARD);

        newQuiz.setMaxScore(quizData.getMaxScore());

        if(quizData.getStatus().equals("PUBLIC")) {
            newQuiz.setQuizStatus(LectureStatus.PUBLIC);
        } else newQuiz.setQuizStatus(LectureStatus.PRIVATE);

        newQuiz.setReleaseDate(new Date(quizData.getReleaseDate()));
        newQuiz.setTime(quizData.getTime());
        newQuiz.setTitle(quizData.getTitle());
        newQuiz.setWeek(weekDb);
        newQuiz.setIndexLecture(quizData.getIndexLecture());
        Quiz quizDb = quizRepository.save(newQuiz);

        //NOTE: create new list question

        for(QuizQuestionResponse question : quizData.getQuestions()) {
            Question newQuestion = new Question();
            newQuestion.setTitle(question.getQuestion());
            newQuestion.setScore(question.getPoint());
            newQuestion.setQuiz(quizDb);
            Question questionDb = questionRepository.save(newQuestion);

            for (QuestionChoiceResponse choice : question.getChoices()) {
                QuestionOption temp = new QuestionOption();
                if(choice.isAnswer()) {
                    temp.setCorrect(true);
                } else temp.setCorrect(false);

                temp.setContent(choice.getChoice());
                temp.setQuestion(questionDb);
                questionOptionRepository.save(temp);
            }
        }

        LectureDto response = new LectureDto();

        response.setIndexLecture(quizData.getIndexLecture());
        response.setLectureStatus(quizData.getStatus());
        response.setLectureType("QUIZ");
        response.setTitle(quizData.getTitle());
        response.setLectureId(quizDb.getID());
        response.setWeekId(weekDb.getID());

        return response;
    }
}
