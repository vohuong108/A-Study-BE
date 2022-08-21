package com.example.astudy.exceptions;

import org.springframework.security.core.AuthenticationException;

public class TokenInvalid extends AuthenticationException {
    public TokenInvalid(String msg) {
        super(msg);
    }

    public TokenInvalid(String msg, Throwable cause) {
        super(msg, cause);
    }

}
