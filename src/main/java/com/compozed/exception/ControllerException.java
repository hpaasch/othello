package com.compozed.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public class ControllerException {
    @ControllerAdvice
    public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

        @ExceptionHandler(value = { Exception.class })
        protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
            String bodyOfResponse = "User Not Found";
            return handleExceptionInternal(ex, bodyOfResponse,
                    new HttpHeaders(), HttpStatus.NOT_FOUND, request);
        }
    }
}
