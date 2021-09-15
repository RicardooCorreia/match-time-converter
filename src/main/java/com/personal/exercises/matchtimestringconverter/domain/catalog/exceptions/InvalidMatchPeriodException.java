package com.personal.exercises.matchtimestringconverter.domain.catalog.exceptions;

public class InvalidMatchPeriodException extends InvalidInputException {

    private InvalidMatchPeriodException(String message) {

        super(message);
    }

    public static InvalidMatchPeriodException forString(String matchPeriod) {

        return new InvalidMatchPeriodException(
            String.format("Given match period with code {%s} is invalid", matchPeriod));
    }
}
