package com.example.mooc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class AuthExceedLoginAttemptsException extends ErrorResponseException {
    public AuthExceedLoginAttemptsException() {
        super(
                HttpStatus.UNAUTHORIZED,
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.UNAUTHORIZED,
                        "Exceed login attempts, connect administration to unblock your account"
                ), null
        );
    }
}
