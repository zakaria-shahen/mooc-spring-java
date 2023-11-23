package com.example.mooc.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
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

}
