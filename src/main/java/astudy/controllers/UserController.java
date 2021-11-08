package astudy.controllers;

import astudy.dtos.UserProfileDto;
import astudy.services.UserService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user/resetpass")
    public ResponseEntity<?> resetPass(@RequestBody ResetPassData resetPassData) {
        boolean result = userService.changePassword(resetPassData.getUsername(),
                resetPassData.getPassword(),
                resetPassData.getNewPass());
        ResetPassResponse response = new ResetPassResponse(
                resetPassData.getUsername(), "reset password successfully");

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/user/changeinfo")
    public ResponseEntity<?> changeInfo(@RequestBody UserProfileDto profileData) {
        UserProfileDto result = userService.changeUserInfo(profileData);

        return ResponseEntity.ok().body(result);
    }

}

@Data
@Getter
@Setter
class ResetPassData {
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("newPass")
    private String newPass;
}

@Data
class ResetPassResponse {
    public ResetPassResponse(String username, String message) {
        this.message = message;
        this.username = username;
    }
    @JsonProperty("username")
    private String username;
    @JsonProperty("message")
    private String message;
}
