package com.personal.exercises.matchtimestringconverter.domain.catalog.exceptions;

public class InputStringDoesNotMatchPatternException extends InvalidInputException {

    private InputStringDoesNotMatchPatternException(String message) {

        super(message);
    }

    public static InvalidInputException forStringAndPattern(String string, String pattern) {

        return new InputStringDoesNotMatchPatternException(
            String.format("Given string {%s} does not match required pattern {%s}", string, pattern));
    }
}
