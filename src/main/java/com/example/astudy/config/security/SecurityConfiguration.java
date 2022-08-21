package com.example.astudy.config.security;

import com.example.astudy.config.security.jwt.JwtAccessDeniedEntryPoint;
import com.example.astudy.filter.CustomAuthorizationFilter;
import com.example.astudy.config.security.jwt.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.astudy.constant.SecurityConstants.*;

/**
 * Problem 1: With the protected url. When don't use ControllerAdvice to catch Exception. Security filter called twice.
 * One for authenticate the coming request and one for exception request (eg: PUT /error).
 * Answer 1: Servlet applying filter based on DispatcherType.
 * url: <a href="https://www.logicbig.com/tutorials/java-ee-tutorial/java-servlet/dispatcher-type.html" />
 * By default, Spring Security's filter only runs async, error, and request dispatches.
 * So, Error request still pass to security filter chain.
 * Set up in application.properties
 *
 *
 * */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedEntryPoint jwtAccessDeniedEntryPoint;
    private final CustomAuthorizationFilter customAuthorizationFilter;

    /**
     * Filter beans are registered with the servlet container automatically.
     * CustomAuthorizationFilter with @Component annotation.
     * CustomAuthorizationFilter is added to servlet filter chain and security filter chain.
     * Hence, it is executed twice (it is also registered twice).
     */
    @Bean
    public FilterRegistrationBean registration(CustomAuthorizationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable(); //because don't use form login
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedEntryPoint);

        http.authorizeRequests().antMatchers(
                API_V1_AUTH_ROOT_URL + "/**",
                API_V1_COMMON_ROOT_URL + "/**"

        ).permitAll();
        http.authorizeRequests().antMatchers(
                API_V1_USER_ROOT_URL + "/**",
                API_V1_COURSE_ROOT_URL + "/**",
                API_V1_QUIZ_ROOT_URL + "/**"
        ).authenticated();
        http.authorizeRequests().antMatchers("/", "/index", "/css/*", "/js/*").permitAll();

        // Used in conjunction with customAuthorizationFilter.
        // any request must need authenticated even if the request 404.
        // when request comes it needs to be authenticated.
        // if the request is not authenticated, an Authority exception will be thrown.
        // Otherwise, it will forward to Matcher Filter then an NotFoundException will be thrown.
//        http.authorizeRequests().anyRequest().authenticated();

        // UsernamePasswordAuthenticationFilter called in an authentication form submission process
        http.addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
