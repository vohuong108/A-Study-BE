package com.example.astudy.config.security.service;

import com.example.astudy.config.security.utils.SecurityUtils;
import com.example.astudy.exceptions.RequestFieldNotFoundException;
import com.example.astudy.repositories.CourseRepo;
import com.example.astudy.repositories.StudentCourseRepo;
import com.example.astudy.repositories.WeekContentRepo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("securityService")
@RequiredArgsConstructor
public class SecurityService {
    private final CourseRepo courseRepo;
    private final StudentCourseRepo studentCourseRepo;
    private final WeekContentRepo weekContentRepo;

    public boolean isAuthorOfCourse(@NonNull Long courseId) {
        String username = SecurityUtils.getAuthenticatedUsername();
        String authorName = courseRepo.findAuthorNameById(courseId);

        if (authorName == null) {
            throw new RequestFieldNotFoundException(
                    String.format("Resource Course's id {%s} not found.", courseId)
            );
        } else {
            return authorName.equals(username);
        }
    }

    public boolean isEnrolled(@NonNull Long courseId) {
        String username = SecurityUtils.getAuthenticatedUsername();
        return studentCourseRepo.checkEnrolledByCourseIdAndStudentName(courseId,username);
    }

    //check when user is either student or not course's author
    public boolean isEnrolledAndPublicContent(@NonNull Long courseId, @NonNull Long contentId) {
        String contentStatus = weekContentRepo.findContentStatusByContentId(contentId);

        if (contentStatus == null) {
            throw new RequestFieldNotFoundException(
                    String.format("Resource WeekContent's id {%s} not found.", contentId)
            );
        }
        if (contentStatus.equals("PRIVATE")) return false;

        return isEnrolled(courseId);
    }

    public boolean isAccessQuiz(@NonNull Long quizId) throws RequestFieldNotFoundException {
        Long courseId = weekContentRepo.findCourseIdByQuizId(quizId);
        if (courseId == null) throw new RequestFieldNotFoundException(
                String.format("Resource Quiz's id {%s} not found.", quizId)
        );

        if (!isAuthorOfCourse(courseId)) {
            return isEnrolledAndPublicContent(courseId, quizId);
        }

        return true;
    }

}
