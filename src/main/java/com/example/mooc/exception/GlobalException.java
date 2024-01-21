package com.example.mooc.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMethod;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Boolean DataIntegrityViolationExceptionHandler(HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getMethod().equals(RequestMethod.DELETE.name())) {
            throw new ResourceTiedToOtherResources();
        }
        throw new ResourceYouTryingToTiedToItDoesntExits();
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ErrorResponse DuplicateKeyExceptionHandler() {
        return new ResourceYouTryToLinkToIsAlreadyLinked();
    }


    @ExceptionHandler(Exception.class)
    @Profile("!prod")
    public ErrorResponse handleUnhandledException(Throwable ex) {
         return ErrorResponse.create(ex, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

}
