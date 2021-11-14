package astudy.services;

import astudy.dtos.LectureDto;
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
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
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
    public RenameWeekData createWeek(RenameWeekData newWeekData) {
        log.info("request create week {}", newWeekData.toString());

        Course courseDb = courseRepository.getById(newWeekData.getCourseId());
        Week newWeek = new Week();
        newWeek.setSerialWeek(newWeekData.getSerialWeek());
        newWeek.setName(newWeekData.getName());
        newWeek.setCourse(courseDb);

        Week result = weekRepository.save(newWeek);

        RenameWeekData response = new RenameWeekData();
        response.setCourseId(newWeekData.getCourseId());
        response.setSerialWeek(newWeekData.getSerialWeek());
        response.setName(newWeekData.getName());
        response.setWeekId(result.getID());

        return response;
    }

    @Override
    public RenameWeekData renameWeek(RenameWeekData renameWeekData) {
        log.info("rename data: {}", renameWeekData.toString());

        Week weekDb = weekRepository.getById(renameWeekData.getWeekId());
        weekDb.setName(renameWeekData.getName());

        Week result = weekRepository.save(weekDb);

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
        response.setUrl("/week/lecture/"
            + result.getLectureType().toString().toLowerCase()
            + "/" + result.getID());

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
        Week weekDb = weekRepository.getById(quizData.getWeekId());

        //TODO: create new quiz
        Quiz newQuiz = new Quiz();

        newQuiz.setAttemptAllow(quizData.getAttemptAllow());
        newQuiz.setCloseDate(Date.from(Instant.parse(quizData.getCloseDate())));

        if(quizData.getDegree().equals("EASY")) {
            newQuiz.setDegree(Degree.EASY);
        } else if(quizData.getDegree().equals("MEDIUM")) {
            newQuiz.setDegree(Degree.MEDIUM);
        } else newQuiz.setDegree(Degree.HARD);

        newQuiz.setMaxScore(quizData.getMaxScore());

        if(quizData.getStatus().equals("PUBLIC")) {
            newQuiz.setQuizStatus(LectureStatus.PUBLIC);
        } else newQuiz.setQuizStatus(LectureStatus.PRIVATE);

        newQuiz.setReleaseDate(Date.from(Instant.parse(quizData.getReleaseDate())));
        newQuiz.setTime(quizData.getTime());
        newQuiz.setTitle(quizData.getTitle());
        newQuiz.setWeek(weekDb);
        newQuiz.setIndexLecture(quizData.getIndexLecture());
        Quiz quizDb = quizRepository.save(newQuiz);

        //TODO: create new list question

        for(QuizQuestionResponse question : quizData.getQuestions()) {
            Question newQuestion = new Question();
            newQuestion.setTitle(question.getQuestion());
            newQuestion.setScore(question.getPoint());
            newQuestion.setQuiz(quizDb);
            Question questionDb = questionRepository.save(newQuestion);

            for (QuestionChoiceResponse choice : question.getChoices()) {
                QuestionOption temp = new QuestionOption();
                temp.setCorrect(choice.isAnswer());

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
        response.setUrl("/week/lecture/quiz/" + quizDb.getID());

        return response;
    }

    @Override
    public EditQuizResponse getQuiz(Long quizId) {
        Quiz quizDb = quizRepository.getById(quizId);

        EditQuizResponse response = new EditQuizResponse();

        List<QuizQuestionResponse> questions = quizDb.getQuestions().stream().map(question -> {
            List<QuestionChoiceResponse> choices = question.getOptions().stream().map(opt -> {
                QuestionChoiceResponse choiceResponse = new QuestionChoiceResponse();
                choiceResponse.setChoiceId(opt.getID());
                choiceResponse.setChoice(opt.getContent());
                choiceResponse.setAnswer(opt.isCorrect());
                return  choiceResponse;
            }).collect(Collectors.toList());

            QuizQuestionResponse questionResponse = new QuizQuestionResponse();
            questionResponse.setQuestionId(question.getID());
            questionResponse.setQuestion(question.getTitle());
            questionResponse.setPoint(question.getScore());
            questionResponse.setChoices(choices);
            return questionResponse;
        }).collect(Collectors.toList());

        response.setQuizId(quizDb.getID());
        response.setTitle(quizDb.getTitle());
        response.setReleaseDate(quizDb.getReleaseDate().toInstant().toString());
        response.setTime(quizDb.getTime());
        response.setCloseDate(quizDb.getCloseDate().toInstant().toString());
        response.setAttemptAllow(quizDb.getAttemptAllow());
        response.setDegree(quizDb.getDegree().toString());
        response.setStatus(quizDb.getQuizStatus().toString());
        response.setQuestions(questions);
        response.setIndexLecture(quizDb.getIndexLecture());

        log.info("response in get quiz {}", response.toString());
        return response;
    }

    @Override
    public LectureDto updateLecture(LectureDto updateLecture) throws IOException {
        log.info("update lecture data {}", updateLecture.toString());

        Lecture lecDb = lectureRepository.findByLectureId(updateLecture.getLectureId());
        LectureContent lecContent = lecDb.getLectureContent();
        lecContent.setContent(updateLecture.getFile().getBytes());
        contentRepository.save(lecContent);

        lecDb.setTitle(updateLecture.getTitle());
        if (updateLecture.getLectureStatus().equals("PUBLIC")) {
            lecDb.setLectureStatus(LectureStatus.PUBLIC);
        } else lecDb.setLectureStatus(LectureStatus.PRIVATE);
        Lecture result = lectureRepository.save(lecDb);

        LectureDto response = new LectureDto();
        response.setWeekId(updateLecture.getWeekId());
        response.setLectureId(updateLecture.getLectureId());
        response.setLectureStatus(updateLecture.getLectureStatus());
        response.setTitle(updateLecture.getTitle());
        return response;
    }

    @Override
    public LectureDto updateQuiz(EditQuizResponse updateQuiz) {
        log.info("update quiuz data {}", updateQuiz);
        Quiz quizDb = quizRepository.getById(updateQuiz.getQuizId());

        quizDb.setAttemptAllow(updateQuiz.getAttemptAllow());
        quizDb.setCloseDate(Date.from(Instant.parse(updateQuiz.getCloseDate())));

        if(updateQuiz.getDegree().equals("EASY")) {
            quizDb.setDegree(Degree.EASY);
        } else if(updateQuiz.getDegree().equals("MEDIUM")) {
            quizDb.setDegree(Degree.MEDIUM);
        } else quizDb.setDegree(Degree.HARD);

        quizDb.setMaxScore(updateQuiz.getMaxScore());

        if(updateQuiz.getStatus().equals("PUBLIC")) {
            quizDb.setQuizStatus(LectureStatus.PUBLIC);
        } else quizDb.setQuizStatus(LectureStatus.PRIVATE);

        quizDb.setReleaseDate(Date.from(Instant.parse(updateQuiz.getReleaseDate())));
        quizDb.setTime(updateQuiz.getTime());
        quizDb.setTitle(updateQuiz.getTitle());

        Quiz resultQuiz = quizRepository.save(quizDb);

        //TODO: delete list question and list option

        for (Question question : quizDb.getQuestions()) {
            questionOptionRepository.deleteAll(question.getOptions());
        }
        questionRepository.deleteAll(quizDb.getQuestions());

        //TODO: create new list question and option
        for(QuizQuestionResponse question : updateQuiz.getQuestions()) {
            Question newQuestion = new Question();
            newQuestion.setTitle(question.getQuestion());
            newQuestion.setScore(question.getPoint());
            newQuestion.setQuiz(resultQuiz);
            Question questionDb = questionRepository.save(newQuestion);

            for (QuestionChoiceResponse choice : question.getChoices()) {
                QuestionOption temp = new QuestionOption();
                temp.setCorrect(choice.isAnswer());

                temp.setContent(choice.getChoice());
                temp.setQuestion(questionDb);
                questionOptionRepository.save(temp);
            }
        }

        //TODO: return information
        LectureDto response = new LectureDto();

        response.setLectureId(resultQuiz.getID());
        response.setTitle(updateQuiz.getTitle());
        response.setLectureType("QUIZ");
        response.setLectureStatus(updateQuiz.getStatus());
        response.setWeekId(updateQuiz.getWeekId());

        log.info("response in update quiz {}", response);

        return response;
    }

    @Override
    public void deleteLecture(Long lectureId) {
        log.info("delete lecture by id {}", lectureId);
        lectureRepository.deleteById(lectureId);
    }

    @Override
    public void deleteQuiz(Long quizId) {
        log.info("delete quiz by id {}", quizId);
        Quiz quizDb = quizRepository.getById(quizId);

        //TODO: delete list question and list option
        for (Question question : quizDb.getQuestions()) {
            questionOptionRepository.deleteAll(question.getOptions());
        }
        questionRepository.deleteAll(quizDb.getQuestions());
        quizRepository.deleteById(quizId);
    }

    @Override
    public void deleteWeekById(Long weekId) {
        List<Long> listQuizId = quizRepository.getListIdByWeekId(weekId);
        List<Long> listLecId = lectureRepository.getListIdByWeekId(weekId);
        log.info("list quiz id {}", listQuizId);
        log.info("list lec id {}", listLecId);
        for (Long quizId : listQuizId) {
            this.deleteQuiz(quizId);
        }

        for (Long lecId : listLecId) {
            this.deleteLecture(lecId);
        }

        weekRepository.deleteById(weekId);
    }
}
