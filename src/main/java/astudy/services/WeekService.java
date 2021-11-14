package astudy.services;

import astudy.dtos.LectureDto;
import astudy.response.EditQuizResponse;
import astudy.response.RenameWeekData;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface WeekService {
    RenameWeekData createWeek(RenameWeekData newWeekData);

    RenameWeekData renameWeek(RenameWeekData renameWeekData);

    LectureDto createLecture(LectureDto newLecture) throws IOException;

    LectureDto updateLecture(LectureDto updateLecture) throws IOException;

    LectureDto createQuiz(EditQuizResponse newQuiz);

    LectureDto updateQuiz(EditQuizResponse updateQuiz);

    byte[] getLectureVideoOrText(Long lectureId);

    EditQuizResponse getQuiz(Long quizId);

    void deleteQuiz(Long quizId);

    void deleteLecture(Long lectureId);

    void deleteWeekById(Long weekId);

}
