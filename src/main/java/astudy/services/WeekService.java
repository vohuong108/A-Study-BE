package astudy.services;

import astudy.dtos.LectureDto;
import astudy.dtos.QuizDto;
import astudy.response.EditQuizResponse;
import astudy.response.RenameWeekData;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface WeekService {
    RenameWeekData renameWeek(RenameWeekData renameWeekData);

    LectureDto createLecture(LectureDto newLecture) throws IOException;

    LectureDto createQuiz(EditQuizResponse newQuiz);

    byte[] getLectureVideoOrText(Long lectureId);

}
