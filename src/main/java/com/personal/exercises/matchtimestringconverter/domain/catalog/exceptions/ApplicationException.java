package com.personal.exercises.matchtimestringconverter.domain.catalog.exceptions;

public abstract class ApplicationException extends RuntimeException {

    public ApplicationException(String message) {
        super(message);
    }
}
