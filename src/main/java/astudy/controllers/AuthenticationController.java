package astudy.controllers;

import astudy.dtos.UserDto;
import astudy.services.AuthService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthenticationController {
    private final AuthService authService;

    @PostMapping("/login")
    private ResponseEntity<?> login(@RequestBody AuthData loginData) {
        UserDto userRes =  authService.login(loginData.getUsername(), loginData.getPassword());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setEmail(userRes.getEmail());
        loginResponse.setUsername(userRes.getUsername());
        loginResponse.setToken("access_token_" + userRes.getUsername());
        loginResponse.setPermission(userRes.getRole());
        return  ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/signup")
    private ResponseEntity<?> signUp(@RequestBody AuthData signUpData) {
        UserDto res = authService.signup(signUpData.getUsername(), signUpData.getEmail(), signUpData.getPassword());
        if (res != null) {
            SignUpResponse signUpResponse = new SignUpResponse();
            signUpResponse.setMessage("SignUp successfully");

            return ResponseEntity.ok().body(signUpResponse);
        }
        return null;
    }

}

@Data
class AuthData {
    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("email")
    private String email;

    @JsonProperty("access_token")
    private String token;
}

@Data
class LoginResponse {
    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("access_token")
    private String token;

    @JsonProperty("permission")
    private String permission;
}

@Data
class SignUpResponse {
    @JsonProperty("message")
    private String message;

}
