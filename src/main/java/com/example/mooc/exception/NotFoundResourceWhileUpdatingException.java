package com.example.mooc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class NotFoundResourceWhileUpdatingException extends ErrorResponseException {

    public NotFoundResourceWhileUpdatingException() {
        super(
                HttpStatus.NOT_FOUND,
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.NOT_FOUND,
                        "Not found resource while update it, please choose correct resource ID"
                ), null
        );
    }
}
