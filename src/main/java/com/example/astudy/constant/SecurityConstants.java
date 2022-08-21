package com.example.astudy.constant;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.List;

public final class SecurityConstants {
    public static final String API_V1_AUTH_ROOT_URL = "/api/v1/auth";
    public static final String API_V1_COMMON_ROOT_URL = "/api/v1/common";
    public static final String API_V1_USER_ROOT_URL = "/api/v1/user";
    public static final String API_V1_COURSE_ROOT_URL = "/api/v1/course";
    public static final String API_V1_QUIZ_ROOT_URL = "/api/v1/quiz";
    public static final String API_ROOT_URL_MAPPING = "/api/**";
    public static final String X_XSRF_TOKEN = "x-xsrf-token";

    public static final List<String> EXCLUDE_URL_PATTERNS =
            List.of(
                    API_V1_AUTH_ROOT_URL + "/**",
                    API_V1_COMMON_ROOT_URL + "/**"
            );

    public static final List<String> AUTHENTICATE_URL_PATTERNS =
            List.of(
                    API_V1_USER_ROOT_URL + "/**",
                    API_V1_COURSE_ROOT_URL + "/**",
                    API_V1_QUIZ_ROOT_URL + "/**"
            );

    public static final List<String> ALLOWED_HTTP_METHODS =
            List.of(
                    HttpMethod.GET.name(),
                    HttpMethod.POST.name(),
                    HttpMethod.PUT.name(),
                    HttpMethod.DELETE.name(),
                    HttpMethod.PATCH.name(),
                    HttpMethod.OPTIONS.name());
    public static final List<String> EXPOSED_HTTP_HEADERS =
            List.of(
                    HttpHeaders.AUTHORIZATION,
                    HttpHeaders.CACHE_CONTROL,
                    HttpHeaders.CONTENT_TYPE,
                    HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                    HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS,
                    HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN);

    public static final List<String> ALLOWED_HTTP_HEADERS =
            List.of(
                    HttpHeaders.AUTHORIZATION,
                    HttpHeaders.CACHE_CONTROL,
                    HttpHeaders.CONTENT_TYPE,
                    X_XSRF_TOKEN,
                    HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                    HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS,
                    HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN);
}
