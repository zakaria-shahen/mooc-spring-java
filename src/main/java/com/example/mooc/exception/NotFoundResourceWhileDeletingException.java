package com.example.mooc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class NotFoundResourceWhileDeletingException extends ErrorResponseException {
    public NotFoundResourceWhileDeletingException() {
        super(
                HttpStatus.NOT_FOUND,
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.NOT_FOUND,
                        "Not found resource while trying to delete it, please choose correct resource ID"
                ), null
        );
    }
}
