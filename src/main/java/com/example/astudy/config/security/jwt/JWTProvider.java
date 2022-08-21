package com.example.astudy.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.astudy.exceptions.TokenInvalid;
import com.example.astudy.services.UserService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Calendar;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@Setter
@Getter
@ConfigurationProperties(prefix = "application.jwt")
@RequiredArgsConstructor
public class JWTProvider implements SmartInitializingSingleton{
    private String secretKey;
    private String tokenPrefix;
    private Integer tokenExpirationAfterMinutes;
    private Integer refreshTokenExpirationAfterDays;
    private String issuer;
    private Algorithm algorithm;

    @Override
    public void afterSingletonsInstantiated() {
        this.algorithm = Algorithm.HMAC256(secretKey);
    }
    private final UserService userService;

    public String generateAccessToken(User user) {
        Calendar c =  Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.MINUTE, tokenExpirationAfterMinutes);

        return JWT.create()
                .withSubject(user.getUsername()) //to identify the user by that specific token
                .withExpiresAt(c.getTime())
                .withIssuer(issuer)
                .withClaim("roles",
                        user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .sign(algorithm);
    }

    public String generateRefreshToken(User user) {
        Calendar c =  Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.DATE, refreshTokenExpirationAfterDays);

        return JWT.create()
                .withSubject(user.getUsername()) //to identify the user by that specific token
                .withExpiresAt(c.getTime())
                .withIssuer(issuer)
                .sign(algorithm);
    }

    /**
     * validate token from request. if token is invalid, it will throw TokenInvalid that's a custom exception.
     * @param token token from request
     * @throws TokenInvalid exception
     * */
    public boolean validateToken(String token) {
        if (token == null) return false;

        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(token);
        return true;
    }

    public String getUsernameFromToken(String token) throws TokenInvalid {
        if(token == null) throw new TokenInvalid("Token must not be null");

        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject();

        } catch (JWTVerificationException e) {
            throw new TokenInvalid(e.getMessage());
        }
    }

    public Map<String, Claim> getClaims(String token) throws TokenInvalid {
        if(token == null) throw new TokenInvalid("Token must not be null");

        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getClaims();

        } catch (JWTVerificationException e) {
            throw new TokenInvalid(e.getMessage());
        }
    }
}
