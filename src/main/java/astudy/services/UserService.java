package astudy.services;

import astudy.dtos.UserProfileDto;
import astudy.response.AllUserAdmin;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    boolean changePassword(String username, String password, String newPass);

    UserProfileDto changeUserInfo(UserProfileDto updateData);

    UserProfileDto getProfile(String username);

    List<AllUserAdmin> getAllUser();

    void changeStatusById(Long userId, String statusType);

    void changeRoleById(Long userId, String roleType);
}
