package astudy.services;

import astudy.dtos.UserDto;
import astudy.enums.Permission;
import astudy.exceptions.AuthException;
import astudy.models.Role;
import astudy.models.User;
import astudy.repositories.RoleRepository;
import astudy.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDto login(String username, String password) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        User userDb = userRepository.findUserByUsername(username);

        if (userDb == null ||
        userDb.getStatus().equals("INACTIVE")) throw new AuthException("username or password is invalid");
        else {
            String passDb = userDb.getPassword();

            if(!encoder.matches(password, passDb)) {
                throw new AuthException("username or password is invalid");
            } else  {
                UserDto result = new UserDto();
                result.setUsername(userDb.getUsername());
                result.setRole(userDb.getRole().getName().toString());
                result.setEmail(userDb.getEmail());

                return result;
            }
        }
    }

    @Override
    public UserDto signup(String username, String email, String password) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        Role userRole = roleRepository.findRoleByName("STUDENT");
        if(userRole == null){
            userRole = roleRepository.save(new Role(Permission.STUDENT));
        }

        User regUser = new User();
        regUser.setUsername(username);
        regUser.setPassword(encoder.encode(password));
        regUser.setEmail(email);
        regUser.setRole(userRole);
        regUser.setStatus("ACTIVE");

        User result = userRepository.save(regUser);

        UserDto res = new UserDto();
        res.setEmail(result.getEmail());
        res.setUsername(result.getUsername());
        res.setRole(result.getRole().getName().toString());
        return res;
    }
}
