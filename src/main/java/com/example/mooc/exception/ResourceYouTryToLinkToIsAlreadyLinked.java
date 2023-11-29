package com.example.mooc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

// To handle/mapping DuplicateKeyException for insert and update operation
public class ResourceYouTryToLinkToIsAlreadyLinked extends ErrorResponseException {
    public ResourceYouTryToLinkToIsAlreadyLinked() {
        super(
                HttpStatus.CONFLICT,
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.CONFLICT,
                        "resource(s) that you attempt to link to already have a link to your resource"
                ), null
        );
    }
}
