package com.example.mooc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class SelectFieldNameNotExists extends ErrorResponseException {
    public SelectFieldNameNotExists() {
        super(
                HttpStatus.BAD_REQUEST,
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.BAD_REQUEST,
                        "The field(s) name you selected is not present. Please check the parameters of your request."
                ), null
        );
    }
}
