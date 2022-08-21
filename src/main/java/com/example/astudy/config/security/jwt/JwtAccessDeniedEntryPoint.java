package com.example.astudy.config.security.jwt;


import com.example.astudy.exceptions.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
public class JwtAccessDeniedEntryPoint implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        log.error("AccessDenied error: {}", accessDeniedException.getMessage());
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getLocalizedMessage());

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(
                response.getOutputStream(),
                new ErrorResponse(HttpStatus.FORBIDDEN, accessDeniedException.getMessage(), request.getRequestURI()));

    }
}
