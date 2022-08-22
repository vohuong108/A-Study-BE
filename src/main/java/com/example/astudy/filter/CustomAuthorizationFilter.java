package com.example.astudy.filter;

import com.auth0.jwt.interfaces.Claim;
import com.example.astudy.config.security.jwt.JWTProvider;
import com.example.astudy.config.security.utils.SecurityUtils;
import com.example.astudy.exceptions.ErrorResponse;
import com.example.astudy.exceptions.TokenInvalid;
import com.example.astudy.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.Map;

import static com.example.astudy.constant.SecurityConstants.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * Problem: In this Filter, getAuthentication return null???
 * If you’re using some other framework that is also filter-based,
 * then you need to make sure that the Spring Security filters come first.
 * This enables the SecurityContextHolder to be populated in time for use by the other filters.
 * This filter placed before FilterChainProxy so Security Context is empty.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final JWTProvider jwtProvider;
    private final UserService userService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException
    {
        String uri = request.getRequestURI();
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            log.info("[{}] Match in have token", uri);
            return false;
        }


        AntPathMatcher pathMatcher = new AntPathMatcher();

        boolean matchIgnoreUrl = EXCLUDE_URL_PATTERNS.stream()
                .anyMatch(p -> pathMatcher.match(p, uri));

        log.info("[{}] matchIgnoreUrl is {}", uri, matchIgnoreUrl);
        if (matchIgnoreUrl) return true;
        else {
            boolean matchedAuthenticateUrl = AUTHENTICATE_URL_PATTERNS.stream()
                    .anyMatch(p -> pathMatcher.match(p, uri));

            log.info("[{}] MatchedAuthenticateUrl is {}", uri, matchedAuthenticateUrl);
            //Ignore query database if request is a missing url page
            return !matchedAuthenticateUrl;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        log.info("[{}] Match in Should Filter", uri);
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring("Bearer ".length());

            try {
                Map<String, Claim> claimMap = jwtProvider.getClaims(token);
                if (claimMap.get("sub").isNull()) {
                    throw new TokenInvalid("Token invalid: sub claim must not null");
                }
                if (claimMap.get("roles") == null || claimMap.get("roles").asList(String.class).isEmpty()) {
                    throw new TokenInvalid("Token invalid: roles claim must not null/empty");
                }
                SecurityUtils.authenticateUser(request, claimMap);

            } catch (TokenInvalid e) {
                log.error("[{}] {}", uri, e.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());

                new ObjectMapper().writeValue(
                        response.getOutputStream(),
                        new ErrorResponse(
                                HttpStatus.UNAUTHORIZED,
                                e.getMessage(),
                                "TOKEN INVALID",
                                uri
                        )
                );

                return;

            } catch (UsernameNotFoundException e) {
                log.error("[{}] {}", uri, e.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());

                new ObjectMapper().writeValue(
                        response.getOutputStream(),
                        new ErrorResponse(
                                HttpStatus.UNAUTHORIZED,
                                String.format("Token valid but %s", e.getMessage()),
                                "UsernameNotFoundException",
                                uri
                        )
                );

                return;
            } catch (Exception e) {
                log.error("Last catch [{}] message {}", uri, e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
