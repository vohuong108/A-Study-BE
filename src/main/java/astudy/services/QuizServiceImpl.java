package astudy.services;

import astudy.models.Quiz;
import astudy.repositories.QuestionOptionRepository;
import astudy.repositories.QuestionRepository;
import astudy.repositories.QuizRepository;
import astudy.response.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class QuizServiceImpl implements QuizService{
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository optionRepository;

    @Override
    public EditQuizResponse getQuizById(Long quizId) {
        Quiz quizDb = quizRepository.getById(quizId);

        EditQuizResponse response = new EditQuizResponse();

        List<QuizQuestionResponse> questions = quizDb.getQuestions().stream().map(question -> {
            List<QuestionChoiceResponse> choices = question.getOptions().stream().map(opt -> {
                QuestionChoiceResponse choiceResponse = new QuestionChoiceResponse();
                choiceResponse.setChoiceId(opt.getID());
                choiceResponse.setChoice(opt.getContent());
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

        log.info("response in get quiz by id {}", response.toString());
        return response;
    }

    @Override
    public QuizResult handleSubmitExamine(SubmitExamine submitExamine) {
        List<Long[]> listQuest = questionRepository.getListIdByQuizId(submitExamine.getQuizId());
        List<Answer> answers = submitExamine.getAnswers();
        Map<Long, Long> ansMap = answers.stream().collect(
                Collectors.toMap(Answer::getQuestionId, Answer::getChoiceId)
        );

        int maxScore = 0;
        int grade = 0;

        for(Long[] questRow : listQuest) {
            maxScore += questRow[1];
            Long correctId = optionRepository.getAnswerByQuestionId(questRow[0]);
            Long ansChoiceId = ansMap.get(questRow[0]);

            if (ansChoiceId.equals(correctId)) {
                grade += questRow[1];
            }
            log.info("quest {} - choice {}", correctId, ansChoiceId);
            log.info("current grade {}", grade);

        }

        QuizResult result = new QuizResult();
        result.setQuizId(submitExamine.getQuizId());
        result.setQuizTitle(submitExamine.getQuizTitle());
        result.setScore("" + grade + "/" + maxScore);

        return result;
    }
}
