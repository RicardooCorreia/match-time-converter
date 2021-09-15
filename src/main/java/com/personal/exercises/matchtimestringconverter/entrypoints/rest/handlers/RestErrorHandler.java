package com.personal.exercises.matchtimestringconverter.entrypoints.rest.handlers;

import com.personal.exercises.matchtimestringconverter.domain.catalog.exceptions.ApplicationException;
import com.personal.exercises.matchtimestringconverter.domain.catalog.exceptions.InvalidInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestErrorHandler {

    @ExceptionHandler(InvalidInputException.class)
    protected ResponseEntity<String> handleInvalidInputException(InvalidInputException exception) {

        log.warn("Invalid input exception", exception);
        return new ResponseEntity<>("INVALID - " + exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationException.class)
    protected ResponseEntity<String> handleApplicationException(ApplicationException exception) {

        log.warn("Application exception", exception);
        return new ResponseEntity<>("Application error - " + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handleException(Exception exception) {

        log.error("Unexpected exception", exception);
        return new ResponseEntity<>("Unexpected error - " + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
