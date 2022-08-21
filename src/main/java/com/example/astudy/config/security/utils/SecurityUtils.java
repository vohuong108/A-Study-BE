package com.example.astudy.config.security.utils;

import com.auth0.jwt.interfaces.Claim;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This utility class holds custom operations on security used in the application.
 *
 * @author Stephen
 * @version 1.0
 * @since 1.0
 */
@Slf4j
public final class SecurityUtils {

    private SecurityUtils() {
        throw new AssertionError("This class cannot be instantiated");
    }

    /**
     * Returns true if the user is authenticated.
     *
     * @param authentication the authentication object
     * @return if user is authenticated
     */
    public static boolean isAuthenticated(Authentication authentication) {
        return Objects.nonNull(authentication)
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }

    /**
     * Returns true if the user is authenticated.
     *
     * @return if user is authenticated
     */
    public static boolean isAuthenticated() {
        return isAuthenticated(getAuthentication());
    }

    /**
     * Retrieve the authentication object from the current session.
     *
     * @return authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Sets the provided authentication object to the SecurityContextHolder.
     *
     * @param authentication the authentication
     */
    public static void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /** Clears the securityContextHolder. */
    public static void clearAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    /**
     * Creates an authentication object with the userDetails then set authentication to
     * SecurityContextHolder.
     *
     * @param userDetails the userDetails
     */
    public static void authenticateUser(UserDetails userDetails) {
        if (Objects.nonNull(userDetails)) {
            var authorities = userDetails.getAuthorities();
            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

            setAuthentication(authentication);
        }
    }

    /**
     * Creates an authentication object with the userDetails then set authentication to
     * SecurityContextHolder.
     *
     * @param claimMap the claim of token.
     * @param request the request come
     */
    public static void authenticateUser(HttpServletRequest request, Map<String, Claim> claimMap) {
        if (Objects.nonNull(request) && Objects.nonNull(claimMap)) {
            String username = claimMap.get("sub").asString();
            List<String> roles = claimMap.get("roles").asList(String.class);

            var authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            setAuthentication(authentication);
        }
    }

    /**
     * Creates an authentication object with the userDetails then set authentication to
     * SecurityContextHolder.
     *
     * @param userDetails the userDetails
     */
    public static void authenticateUser(HttpServletRequest request, UserDetails userDetails) {
        if (Objects.nonNull(request) && Objects.nonNull(userDetails)) {
            var authorities = userDetails.getAuthorities();
            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            setAuthentication(authentication);
        }
    }

    /**
     * Creates an authentication object with the credentials then set authentication to
     * SecurityContextHolder.
     *
     * @param authenticationManager the authentication manager
     * @param username the username
     * @param password the password
     */
    public static void authenticateUser (
            AuthenticationManager authenticationManager, String username, String password) {

        var authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        //Call loadUserByUsername
        var authentication = authenticationManager.authenticate(authenticationToken);

        setAuthentication(authentication);
    }



    /**
     * Returns the user details from the authenticated object if authenticated.
     *
     * @return the user details
     */
    public static User getAuthenticatedUserDetails() {
        if (isAuthenticated()) {
            return (User) getAuthentication().getPrincipal();
        }
        return null;
    }

    /**
     * Returns username from the authenticated object if authenticated.
     * Authentication object format is
     * + principal: username
     * + credential: null
     * + authorities: roles
     *
     * @return username
     */
    public static String getAuthenticatedUsername() {
        if (isAuthenticated()) {
            return (String) getAuthentication().getPrincipal();
        }
        return null;
    }

    public static Set<String> getAuthenticatedRole() {
        if (isAuthenticated()){
            return getAuthentication()
                    .getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        }
        return null;
    }

    /**
     * Logout the user from the system and clear all cookies from request and response.
     *
     * @param request the request
     * @param response the response
     */
    public static void logout(HttpServletRequest request, HttpServletResponse response) {

        String rememberMeCookieKey = AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY;
        CookieClearingLogoutHandler logoutHandler =
                new CookieClearingLogoutHandler(rememberMeCookieKey);

        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, null);
        securityContextLogoutHandler.logout(request, response, null);
    }
}



