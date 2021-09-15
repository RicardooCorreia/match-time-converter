package com.personal.exercises.matchtimestringconverter.domain.catalog.exceptions;

public abstract class InvalidInputException extends ApplicationException {

    public InvalidInputException(String message) {

        super(message);
    }
}
