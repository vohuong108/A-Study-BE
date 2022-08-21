package com.example.astudy.config.security.share;

import com.example.astudy.config.security.utils.SecurityUtils;
import com.example.astudy.entities.AppUser;
import com.example.astudy.entities.Course;
import com.example.astudy.entities.Week;
import com.example.astudy.entities.WeekContent;
import com.example.astudy.enums.AppUserRole;
import com.example.astudy.exceptions.RequestFieldNotFoundException;
import com.example.astudy.repositories.CourseRepo;
import com.example.astudy.repositories.StudentCourseRepo;
import com.example.astudy.repositories.WeekContentRepo;
import com.example.astudy.repositories.WeekRepo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ValidateCourseResource {
    private final CourseRepo courseRepo;
    private final WeekRepo weekRepo;
    private final WeekContentRepo weekContentRepo;
    private final StudentCourseRepo studentCourseRepo;

    public Course checkCourseExists(@NonNull Long courseId, boolean isReturn) throws RequestFieldNotFoundException {
        if (isReturn) {
            Course course = courseRepo.findCourseByID(courseId);
            if (course == null) {
                throw new RequestFieldNotFoundException(String.format("Course with id {%s} not found", courseId));
            }
            return course;
        } else {
            if (courseRepo.checkCourseExist(courseId)) {
                return null;
            } else {
                throw new RequestFieldNotFoundException(String.format("Course with id {%s} not found", courseId));
            }
        }
    }

    public Course checkCourseExists(@NonNull Long courseId) throws RequestFieldNotFoundException {
        return checkCourseExists(courseId, true);
    }

    public Week checkWeekExists(@NonNull Long courseId, @NonNull Long weekId) throws RequestFieldNotFoundException {
        Week week = weekRepo.findWeekByWeekIDAndCourseID(weekId, courseId);

        if (week == null) {
            throw new RequestFieldNotFoundException(
                    String.format("Resource with Course's id {%s} and Week's id {%s} not found", courseId, weekId)
            );
        }

        return week;
    }

    public WeekContent checkWeekContentExists(
            @NonNull Long courseId,
            @NonNull Long weekId,
            @NonNull Long contentId,
            boolean isReturn
    ) throws RequestFieldNotFoundException {
        Long curWeekId = weekContentRepo.findWeekIdByWeekContentId(contentId);

        if (!Objects.equals(curWeekId, weekId)) {
            throw new RequestFieldNotFoundException(
                    String.format(
                            "Resource with Week's id {%s} and WeekContent's id {%s} not found",
                            weekId, contentId
                    )
            );
        }

        checkWeekExists(courseId, weekId);

        if (isReturn) return weekContentRepo.findWeekContentByID(contentId);
        else return null;
    }

    public WeekContent checkWeekContentExists(
            @NonNull Long courseId,
            @NonNull Long weekId,
            @NonNull Long contentId
    ) throws RequestFieldNotFoundException {
        return checkWeekContentExists(courseId, weekId, contentId, true);
    }

    public boolean checkFullAccess(@NonNull Long courseId) throws RequestFieldNotFoundException, AccessDeniedException {
        checkCourseExists(courseId, false);

        String username = SecurityUtils.getAuthenticatedUsername();
        var roles = SecurityUtils.getAuthenticatedRole();

        if (Objects.requireNonNull(roles).contains("ROLE_SUPER_ADMIN") ||
                Objects.requireNonNull(roles).contains("ROLE_ADMIN_TRAINEE")
        ) {
            return true;

        } else if (Objects.requireNonNull(roles).contains("ROLE_AUTHOR")) {
            String authorName = courseRepo.findAuthorNameById(courseId);

            if (authorName == null) {
                throw new RequestFieldNotFoundException(
                        String.format("Resource with Course's id {%s} not found", courseId)
                );
            } else if(authorName.equals(username)) {
                return true;
            }

        }

        //check account with author role either enrolled this course or not
        if(studentCourseRepo.checkEnrolledByCourseIdAndStudentName(courseId,username)) {
            return false;
        } else {
            throw new AccessDeniedException("You do not have access to Course's id " + courseId);
        }
    }
}
