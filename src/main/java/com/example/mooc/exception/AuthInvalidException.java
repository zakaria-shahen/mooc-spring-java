package com.example.mooc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class AuthInvalidException extends ErrorResponseException {
    public AuthInvalidException() {
        super(
                HttpStatus.UNAUTHORIZED,
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid"
                ), null
        );
    }
}
