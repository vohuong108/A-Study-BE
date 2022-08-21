package com.example.astudy.config.security.jwt;

import com.example.astudy.exceptions.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * This class implements AuthenticationEntryPoint interface. Then we override the commence method.
 * This method will be triggered anytime unauthenticated User requests a secured endpoint and an
 * AuthenticationException is thrown.
 *
 * @author vohuong108
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    //TODO: entry point catch exception from JWTFilter
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException {

        log.error("Authentication error: {}", authException.getMessage());
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getLocalizedMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(
                response.getOutputStream(),
                new ErrorResponse(HttpStatus.UNAUTHORIZED, authException.getMessage(), request.getRequestURI()));
    }

}
