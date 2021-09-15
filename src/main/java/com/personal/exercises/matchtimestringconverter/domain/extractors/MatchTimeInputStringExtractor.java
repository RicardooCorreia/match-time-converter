package com.personal.exercises.matchtimestringconverter.domain.extractors;

import com.personal.exercises.matchtimestringconverter.domain.catalog.MatchInput;
import com.personal.exercises.matchtimestringconverter.domain.catalog.MatchPeriod;
import com.personal.exercises.matchtimestringconverter.domain.catalog.MatchPeriodEnum;
import com.personal.exercises.matchtimestringconverter.domain.catalog.exceptions.InputStringDoesNotMatchPatternException;
import com.personal.exercises.matchtimestringconverter.domain.catalog.exceptions.InvalidMatchPeriodException;
import com.personal.exercises.matchtimestringconverter.domain.constants.RegexConstants;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MatchTimeInputStringExtractor {

    private static final Pattern REGEX_INPUT_PATTERN = Pattern.compile(RegexConstants.MATCH_INPUT_REGEX);

    public MatchInput extract(String inputString) {

        final String trimmedInput = inputString.trim();
        final Matcher matcher = REGEX_INPUT_PATTERN.matcher(trimmedInput);

        if (matcher.find()) {
            return extractMatchInput(matcher);
        } else {
            throw InputStringDoesNotMatchPatternException.forStringAndPattern(inputString, RegexConstants.MATCH_INPUT_REGEX);
        }
    }

    private MatchInput extractMatchInput(Matcher matcher) {

        final MatchPeriod period = getPeriod(matcher);
        final Duration time = getTime(matcher);
        return MatchInput.of(period, time);
    }

    private Duration getTime(Matcher matcher) {

        final String minutesGroup = matcher.group(RegexConstants.MINUTES_GROUP);
        final String secondsGroup = matcher.group(RegexConstants.SECONDS_GROUP);
        final String millisecondsGroup = matcher.group(RegexConstants.MILLISECONDS_GROUP);
        return Duration
            .ofMinutes(Integer.parseInt(minutesGroup))
            .plusSeconds(Integer.parseInt(secondsGroup))
            .plusMillis(Integer.parseInt(millisecondsGroup));
    }

    private MatchPeriod getPeriod(Matcher matcher) {

        final String periodString = matcher.group(RegexConstants.PERIOD_GROUP);
        return MatchPeriodEnum.valueOfShortForm(periodString)
            .orElseThrow(() -> InvalidMatchPeriodException.forString(periodString));
    }
}
