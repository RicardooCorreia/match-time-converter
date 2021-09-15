package com.personal.exercises.matchtimestringconverter.domain.catalog.exceptions;

import com.personal.exercises.matchtimestringconverter.domain.catalog.MatchPeriod;

import java.time.Duration;

public class InvalidTimeForPeriodException extends InvalidInputException {

    private InvalidTimeForPeriodException(String message) {

        super(message);
    }

    public static InvalidTimeForPeriodException forMinimumTime(Duration actualTime, Duration minimumTime, MatchPeriod matchPeriod) {

        return new InvalidTimeForPeriodException(
            String.format("Given time {%s} is before minimum time {%s} for requested period {%s}",
                formatDuration(actualTime),
                formatDuration(minimumTime),
                matchPeriod.getLongForm()));
    }

    public static InvalidTimeForPeriodException forFixedTime(Duration actualTime, Duration fixedTime, MatchPeriod matchPeriod) {

        return new InvalidTimeForPeriodException(
            String.format("Given time {%s} is invalid, it should always be {%s} for requested period {%s}",
                formatDuration(actualTime),
                formatDuration(fixedTime),
                matchPeriod.getLongForm()));
    }

    private static String formatDuration(Duration duration) {

        return String.format("%02d:%02d.%03d", duration.toMinutes(), duration.toSecondsPart(), duration.toMillisPart());
    }
}
