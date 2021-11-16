package astudy.controllers;

import astudy.dtos.UserProfileDto;
import astudy.services.UserService;
import astudy.utils.HandleToken;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api")
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/user/resetpass")
    public ResponseEntity<?> resetPass(@RequestBody ResetPassData resetPassData) {

        log.info("reset pass data: {}", resetPassData.toString());

        boolean result = userService.changePassword(resetPassData.getUsername(),
                resetPassData.getPassword(),
                resetPassData.getNewPass());
        ResetPassResponse response = new ResetPassResponse(
                resetPassData.getUsername(), "reset password successfully");

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/user/changeinfo")
    public ResponseEntity<?> changeInfo(@RequestBody UserProfileDto profileData) {
        log.info("change data: {}", profileData.toString());
        UserProfileDto result = userService.changeUserInfo(profileData);
        log.info("changed data: {}", result.toString());
        log.info("res: {}", ResponseEntity.ok().body(result).getBody().toString());
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/user/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String token) {
        log.info("Auth string: {}", token);
        String username = HandleToken.getUsernameFromToken(token);

        log.info("username: {}", username);
        UserProfileDto currentUser = userService.getProfile(username);

        return ResponseEntity.ok().body(currentUser);

    }

    @GetMapping("/users/admin")
    public ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok().body(userService.getAllUser());

    }

    @PutMapping ("/user/changestatus/{userId}/{type}")
    public ResponseEntity<?> changeStatus(
            @PathVariable("userId") Long userId,
            @PathVariable("type") String statusType) {
        userService.changeStatusById(userId, statusType);
        return ResponseEntity.ok().body(
                new DeleteUserMessage("Change user status successfully")
        );
    }

    @PutMapping("/user/changestatus/{type}")
    public ResponseEntity<?> deleteManyUser(
            @PathVariable("type") String statusType,
            @RequestBody DeleteManyUser data) {
        log.info("list delete user: {}", data.getListUserId());
        for (Long userId : data.getListUserId()) {
            userService.changeStatusById(userId, statusType);
        }
        return ResponseEntity.ok().body(
                new DeleteUserMessage("Change user status successfully")
        );
    }

    @PutMapping ("/user/changerole/{userId}/{type}")
    public ResponseEntity<?> changeRole(
            @PathVariable("userId") Long userId,
            @PathVariable("type") String roleType) {
        userService.changeRoleById(userId, roleType);
        return ResponseEntity.ok().body(
                new DeleteUserMessage("Change user role successfully")
        );
    }

}

@Data
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

@Data
class DeleteUserMessage {
    public DeleteUserMessage(String message) {
        this.message = message;
    }
    @JsonProperty("message")
    private String message;
}

@Data
class DeleteManyUser {
    @JsonProperty("ids")
    private List<Long> listUserId;
}
