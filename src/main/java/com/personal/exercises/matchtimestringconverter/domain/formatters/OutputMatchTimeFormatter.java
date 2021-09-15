package com.personal.exercises.matchtimestringconverter.domain.formatters;

import com.personal.exercises.matchtimestringconverter.domain.catalog.MatchPeriod;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class OutputMatchTimeFormatter {

    public static final String PATTERN = "%02d:%02d";

    public static final String SEPARATOR = " - ";

    public String format(Duration time, MatchPeriod matchPeriod) {

        final String timeString = formatMatchTime(time);
        return concat(timeString, SEPARATOR, matchPeriod.getLongForm());
    }

    public String format(Duration time, Duration extra, MatchPeriod period) {

        final String timeString = formatMatchTime(time);
        final String extraString = formatMatchTime(extra);
        return concat(timeString, " +", extraString, SEPARATOR, period.getLongForm());
    }

    private String formatMatchTime(Duration time) {

        return String.format(PATTERN, time.toMinutes(), time.toSecondsPart());
    }

    public String concat(String... strings) {

        final StringBuilder stringBuilder = new StringBuilder();

        for (String string : strings) {
            stringBuilder.append(string);
        }

        return stringBuilder.toString();
    }
}
