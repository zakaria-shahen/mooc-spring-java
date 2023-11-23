package com.example.mooc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

// To handle/mapping DataIntegrityViolationException for delete operation
public class ResourceTiedToOtherResources extends ErrorResponseException {
    public ResourceTiedToOtherResources() {
        super(
                HttpStatus.CONFLICT,
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.CONFLICT,
                        "resource tied to other resources"
                ), null
        );
    }
}
