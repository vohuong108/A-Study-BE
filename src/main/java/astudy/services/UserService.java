package astudy.services;

import astudy.dtos.CourseDto;
import astudy.dtos.UserProfileDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<CourseDto> findListUserCourse(Long userId);

    boolean changePassword(String username, String password, String newPass);

    UserProfileDto changeUserInfo(UserProfileDto updateData);

    UserProfileDto getProfile(String username);
}
