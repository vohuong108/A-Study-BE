package com.example.astudy.filter;

import com.example.astudy.constant.SecurityConstants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * CORS must be processed before Spring Security
 * Add Component annotation to this class.
 * Add this bean before SessionManagementFilter to your security configuration.
 * */

public class CustomCorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request= (HttpServletRequest) servletRequest;

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", String.valueOf(SecurityConstants.ALLOWED_HTTP_METHODS));
        response.setHeader("Access-Control-Allow-Headers", String.valueOf(SecurityConstants.ALLOWED_HTTP_HEADERS));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", "180");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
