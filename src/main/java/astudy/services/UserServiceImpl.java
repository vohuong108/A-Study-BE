package astudy.services;

import astudy.dtos.UserProfileDto;
import astudy.exceptions.AuthException;
import astudy.models.User;
import astudy.models.UserProfile;
import astudy.repositories.UserProfileRepository;
import astudy.repositories.UserRepository;
import astudy.response.AllCourseAdmin;
import astudy.response.AllUserAdmin;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public boolean changePassword(String username, String password, String newPass) {
        User userDb = userRepository.findUserByUsername(username);

        if (userDb == null) throw new AuthException("username or password is invalid");
        else {
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            String passDb = userDb.getPassword();

            if(!encoder.matches(password, passDb)) {
                throw new AuthException("username or password is invalid");
            } else  {
                userDb.setPassword(encoder.encode(newPass));

                User result = userRepository.save(userDb);

                if (result != null) return true;
                else return false;
            }
        }
    }

    @Override
    public UserProfileDto changeUserInfo(UserProfileDto updateData) {
        User userDb = userRepository.findUserByUsername(updateData.getUsername());

        if (userDb == null) throw new AuthException("username is invalid");

        UserProfile userProfile = userDb.getUserProfile();

        if(userProfile == null) userProfile = new UserProfile();

        if(updateData.getAddress() != null) {
            userProfile.setAddress(updateData.getAddress());
            log.info(updateData.getAddress());

        }

        if(updateData.getFirstName() != null) {
            userProfile.setFirstName(updateData.getFirstName());
            log.info(updateData.getFirstName());
        }

        if(updateData.getLastName() != null) {
            userProfile.setLastName(updateData.getLastName());
            log.info(updateData.getLastName());
        }

        if(updateData.getPhone() != null) {
            userProfile.setPhone(updateData.getPhone());
        }

        userDb.setUserProfile(userProfileRepository.save(userProfile));
        userRepository.save(userDb);

        return getProfile(userDb.getUsername());
    }

    @Override
    public UserProfileDto getProfile(String username) {
        User userDb = userRepository.findUserByUsername(username);
        UserProfile profileDb = userDb.getUserProfile();
        UserProfileDto response = new UserProfileDto();

        response.setEmail(userDb.getEmail());
        response.setUsername(username);
        response.setPermission(userDb.getRole().getName().toString());

        if(profileDb != null) {
            response.setFirstName(profileDb.getFirstName());
            response.setLastName(profileDb.getLastName());
            response.setPhone(profileDb.getPhone());
            response.setAddress(profileDb.getAddress());
        }

        return response;

    }

    @Override
    public List<AllUserAdmin> getAllUser() {
        String[][] allUserDb = userRepository.findAllUser();
        log.info("all userDb row length: {}", allUserDb.length);

        List<AllUserAdmin> users = new ArrayList<>();
        //SELECT u.ID, u.email, u.username, r.name AS role ,u.status
        for(String[] row : allUserDb) {
            AllUserAdmin temp = new AllUserAdmin();
            temp.setUserId(Long.parseLong(row[0]));
            temp.setEmail(row[1]);
            temp.setUsername(row[2]);
            temp.setUserRole(row[3]);
            temp.setStatus(row[4]);
            users.add(temp);
        }

        return users;
    }

    @Override
    public void changeStatusById(Long userId, String statusType) {
        userRepository.updateUserStatus(userId, statusType);
    }

    @Override
    public void changeRoleById(Long userId, String roleType) {
        switch (roleType.toLowerCase()) {
            case "student":
                userRepository.updateUserRole(userId, "STUDENT");
                break;
            case "author":
                userRepository.updateUserRole(userId, "AUTHOR");
                break;
            case "admin":
                userRepository.updateUserRole(userId, "ADMIN");
                break;
        }
    }
}
