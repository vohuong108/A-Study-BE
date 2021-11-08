package astudy.services;

import astudy.dtos.UserDto;
import astudy.exceptions.AuthException;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    UserDto login(String username, String password) throws AuthException;
    UserDto signup(String username, String email, String password) throws AuthException;
}
