package com.example.mooc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class SomethingWantWrongWhileFetchingIdException extends ErrorResponseException {
    public SomethingWantWrongWhileFetchingIdException() {
        super(
                HttpStatus.NOT_FOUND,
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Something Want Wrong, unable to get resource ID!, try again later"
                ), null
        );
    }
}
