package com.example.mooc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

// To handle/mapping DataIntegrityViolationException for create and update operation
public class ResourceYouTryingToTiedToItDoesntExits extends ErrorResponseException {
    public ResourceYouTryingToTiedToItDoesntExits() {
        super(
                HttpStatus.CONFLICT,
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.CONFLICT,
                        "The resource you trying to tied to it doesn't exits"
                ), null
        );
    }
}
