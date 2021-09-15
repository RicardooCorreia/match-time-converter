package com.personal.exercises.matchtimestringconverter.domain.use.cases.impl;

import com.personal.exercises.matchtimestringconverter.domain.catalog.MatchInput;
import com.personal.exercises.matchtimestringconverter.domain.catalog.MatchPeriod;
import com.personal.exercises.matchtimestringconverter.domain.catalog.exceptions.InvalidTimeForPeriodException;
import com.personal.exercises.matchtimestringconverter.domain.extractors.MatchTimeInputStringExtractor;
import com.personal.exercises.matchtimestringconverter.domain.formatters.OutputMatchTimeFormatter;
import com.personal.exercises.matchtimestringconverter.domain.use.cases.ConvertMatchTimeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class ConvertMatchTimeWithFixedMeanTimesUseCaseImpl implements ConvertMatchTimeUseCase {

    private final MatchTimeInputStringExtractor matchTimeInputStringExtractor;

    private final OutputMatchTimeFormatter outputMatchTimeFormatter;

    @Override
    public String convert(String inputString) {

        final MatchInput matchInput = matchTimeInputStringExtractor.extract(inputString);
        final Duration time = matchInput.getTime();
        final MatchPeriod period = matchInput.getPeriod();

        if (matchInput.getPeriod().isMeanTime()) {
            return processFixedTime(time, period);
        } else {
            return processTime(time, period);
        }
    }

    private String processTime(Duration time, MatchPeriod period) {

        final Duration baseTime = period.getInitialTime();
        shouldBeBiggerThan(time, baseTime, period);

        final Duration finalTime = period.getFinalTime();
        final Duration difference = difference(time, finalTime);

        return difference.isNegative() || difference.isZero()
            ? outputMatchTimeFormatter.format(roundToSeconds(time), period)
            : outputMatchTimeFormatter.format(finalTime, roundToSeconds(difference), period);
    }

    private Duration roundToSeconds(Duration time) {

        final int millisPart = time.toMillisPart();
        final int halfSecondInMilli = 500;
        final int maxNumberOfSeconds = 60;
        final int extraSecond = (millisPart / halfSecondInMilli) % maxNumberOfSeconds;
        return time
            .truncatedTo(ChronoUnit.SECONDS)
            .plusSeconds(extraSecond);
    }

    private String processFixedTime(Duration time, MatchPeriod matchPeriod) {

        final Duration baseTime = matchPeriod.getInitialTime();
        final Duration difference = difference(time, baseTime);
        if (difference.isZero()) {
            return outputMatchTimeFormatter.format(baseTime, matchPeriod);
        } else {
            throw InvalidTimeForPeriodException.forFixedTime(time, baseTime, matchPeriod);
        }
    }

    private Duration difference(Duration time1, Duration time2) {

        return time1.minus(time2);
    }

    private void shouldBeBiggerThan(Duration timeToValidate,
                                    Duration minimumTime,
                                    MatchPeriod period) {

        final Duration difference = timeToValidate.minus(minimumTime);
        if (difference.isNegative()) {
            throw InvalidTimeForPeriodException.forMinimumTime(timeToValidate, minimumTime, period);
        }
    }
}
