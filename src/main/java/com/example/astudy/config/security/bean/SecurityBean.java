package com.example.astudy.config.security.bean;

import com.example.astudy.config.properties.CorsConfigProperties;
import com.example.astudy.constant.SecurityConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static com.example.astudy.constant.SecurityConstants.ALLOWED_HTTP_HEADERS;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityBean {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configures cors for all requests towards the API.
     * CORS must be processed before Spring Security because the pre-flight request will not contain any cookies (i.e. the JSESSIONID).
     * If the request does not contain any cookies and Spring Security is first,
     * the request will determine the user is not authenticated (since there are no cookies in the request) and reject it.
     * The easiest way to ensure that CORS is handled first is to use the CorsFilter.
     * Users can integrate the CorsFilter with Spring Security by providing a CorsConfigurationSource.
     *
     * @return CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource(final CorsConfigProperties props) {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowCredentials(props.isAllowCredentials());
        corsConfiguration.setMaxAge(Duration.ofHours(props.getMaxAge()));

        setExposedHeaders(props, corsConfiguration);
        setAllowedHeaders(props, corsConfiguration);
        setAllowedMethods(props, corsConfiguration);
        corsConfiguration.setAllowedOrigins(props.getAllowedOrigins());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(SecurityConstants.API_ROOT_URL_MAPPING, corsConfiguration);

        return source;
    }

    /**
     * Set the list of response headers other than simple headers (i.e. {@code Cache-Control}, {@code
     * Content-Language}, {@code Content-Type}, {@code Expires}, {@code Last-Modified}, or {@code
     * Pragma}) that an actual response might have and can be exposed.
     *
     * <p>The special value {@code "*"} allows all headers to be exposed for non-credentialed
     * requests.
     *
     * <p>By default this is not set.
     *
     * @param props CorsConfigProperties
     * @param corsConfig CorsConfiguration
     */
    private void setExposedHeaders(CorsConfigProperties props, CorsConfiguration corsConfig) {
        if (CollectionUtils.isEmpty(props.getExposedHeaders())) {
            corsConfig.setExposedHeaders(SecurityConstants.EXPOSED_HTTP_HEADERS);
        } else {
            corsConfig.setExposedHeaders(props.getExposedHeaders());
        }
    }

    /**
     * Set the list of headers that a pre-flight request can list as allowed for use during an actual
     * request.
     *
     * <p>The special value {@code "*"} allows actual requests to send any header.
     *
     * <p>A header name is not required to be listed if it is one of: {@code Cache-Control}, {@code
     * Content-Language}, {@code Expires}, {@code Last-Modified}, or {@code Pragma}.
     *
     * <p>By default this is not set.
     *
     * @param props CorsConfigProperties
     * @param corsConfig CorsConfiguration
     */
    private void setAllowedHeaders(CorsConfigProperties props, CorsConfiguration corsConfig) {
        if (CollectionUtils.isEmpty(props.getAllowedHeaders())) {
            corsConfig.setAllowedHeaders(ALLOWED_HTTP_HEADERS);
        } else {
            corsConfig.setAllowedHeaders(props.getAllowedHeaders());
        }
    }

    /**
     * Set the HTTP methods to allow, e.g. {@code "GET"}, {@code "POST"}, {@code "PUT"}, etc.
     *
     * <p>The special value {@code "*"} allows all methods.
     *
     * <p>If not set, only {@code "GET"} and {@code "HEAD"} are allowed.
     *
     * <p>By default this is not set.
     *
     * <p><strong>Note:</strong> CORS checks use values from "Forwarded"
     *
     * <p>(<a href="https://tools.ietf.org/html/rfc7239">RFC 7239</a>), "X-Forwarded-Host",
     * "X-Forwarded-Port", and "X-Forwarded-Proto" headers, if present, in order to reflect the
     * client-originated address. Consider using the {@code ForwardedHeaderFilter} in order to choose
     * from a central place whether to extract and use, or to discard such headers. See the Spring
     * Framework reference for more on this filter.
     *
     * @param props CorsConfigProperties
     * @param corsConfig CorsConfiguration
     */
    private void setAllowedMethods(CorsConfigProperties props, CorsConfiguration corsConfig) {
        if (CollectionUtils.isEmpty(props.getAllowedMethods())) {
            corsConfig.setAllowedMethods(SecurityConstants.ALLOWED_HTTP_METHODS);
        } else {
            corsConfig.setAllowedMethods(props.getAllowedMethods());
        }
    }
}
