package com.example.astudy.api;

import com.example.astudy.dtos.ResponseBodyInfo;
import com.example.astudy.dtos.UserDto;
import com.example.astudy.enums.AccountStatus;
import com.example.astudy.enums.AppUserRole;
import com.example.astudy.exceptions.TokenInvalid;
import com.example.astudy.config.security.jwt.JWTProvider;
import com.example.astudy.config.security.utils.SecurityUtils;
import com.example.astudy.services.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static com.example.astudy.constant.SecurityConstants.API_V1_AUTH_ROOT_URL;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@Slf4j
@RestController
@RequestMapping(API_V1_AUTH_ROOT_URL)
@RequiredArgsConstructor
public class AuthResource {
    private final JWTProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    //TODO: Persistence account, cookie

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, @RequestBody LoginForm loginForm) {
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();

        // Authentication will fail if the credentials are invalid and throw exception.
        SecurityUtils.authenticateUser(authenticationManager, username, password);

        //In this line, user absolutely exist.
        User user = SecurityUtils.getAuthenticatedUserDetails();

        String access_token = jwtProvider.generateAccessToken(user);
        String refresh_token = jwtProvider.generateRefreshToken(user);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);

        String uri = request.getRequestURI();
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ResponseBodyInfo(uri, tokens));

    }

    @GetMapping("/token/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith(jwtProvider.getTokenPrefix())) {
            String refresh_token = authorizationHeader.substring(jwtProvider.getTokenPrefix().length());

            // throw exception if token invalid. Exception centrally handled
            String username = jwtProvider.getUsernameFromToken(refresh_token);
            // throw exception if username that decoded from token is not found. Exception centrally handled
            User user = (User) userService.loadUserByUsername(username);

            String access_token = jwtProvider.generateAccessToken(user);

            String uri = request.getRequestURI();
            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", access_token);
            tokens.put("refresh_token", refresh_token);

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ResponseBodyInfo(uri, tokens));

        } else throw new TokenInvalid("Refresh token is missing");

    }

    @PostMapping("signup")
    public ResponseEntity<?> signup(
            HttpServletRequest request,
            @RequestBody SignUpForm signUpForm) throws URISyntaxException {

        UserDto user = new UserDto(
                null,
                signUpForm.getUsername(),
                signUpForm.getPassword(),
                signUpForm.getEmail(),
                AppUserRole.STUDENT,
                AccountStatus.ACTIVE,
                null
        );

        return ResponseEntity.created(new URI(request.getRequestURI()))
                .body(userService.saveUser(user));
    }

}

@Data
class LoginForm {
    private String username;
    private String password;
}

@Data
class SignUpForm {
    private String username;
    private String password;
    private String email;
    private boolean checkbox;
}