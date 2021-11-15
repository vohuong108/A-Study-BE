package astudy.services;

import astudy.response.EditQuizResponse;
import astudy.response.QuizResult;
import astudy.response.SubmitExamine;
import org.springframework.stereotype.Service;

@Service
public interface QuizService {
    EditQuizResponse getQuizById(Long quizId);

    QuizResult handleSubmitExamine(SubmitExamine submitExamine);
}
