package com.personal.exercises.matchtimestringconverter.domain.formatters;

import com.personal.exercises.matchtimestringconverter.domain.catalog.MatchPeriodEnum;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class OutputTimeFormatterTest {

    private final OutputMatchTimeFormatter subject = new OutputMatchTimeFormatter();

    @Test
    void format_whenMinutesOrSecondsAreLessThan10_addLeadingZeros() {

        // Given
        final Duration duration = Duration.ofMinutes(1).plusSeconds(5);
        final MatchPeriodEnum matchPeriod = MatchPeriodEnum.H1;

        // When
        final String result = subject.format(duration, matchPeriod);

        // Then
        assertThat(result)
            .isEqualTo("01:05 - FIRST_HALF");
    }

    @Test
    void formatWithExtra_whenMinutesOrSecondsAreLessThan10_addLeadingZeros() {

        // Given
        final Duration duration = Duration.ofMinutes(1).plusSeconds(5);
        final Duration extra = Duration.ofMinutes(2).plusSeconds(9);
        final MatchPeriodEnum matchPeriod = MatchPeriodEnum.H1;

        // When
        final String result = subject.format(duration, extra, matchPeriod);

        // Then
        assertThat(result)
            .isEqualTo("01:05 +02:09 - FIRST_HALF");
    }

    @Test
    void format_whenMinutesOrSecondsAreMoreThan10_formatNormally() {

        // Given
        final Duration duration = Duration.ofMinutes(17).plusSeconds(25);
        final MatchPeriodEnum matchPeriod = MatchPeriodEnum.H1;

        // When
        final String result = subject.format(duration, matchPeriod);

        // Then
        assertThat(result)
            .isEqualTo("17:25 - FIRST_HALF");
    }

    @Test
    void formatWithExtra_whenMinutesOrSecondsAreMoreThan10_formatNormally() {

        // Given
        final Duration duration = Duration.ofMinutes(10).plusSeconds(54);
        final Duration extra = Duration.ofMinutes(24).plusSeconds(38);
        final MatchPeriodEnum matchPeriod = MatchPeriodEnum.H1;

        // When
        final String result = subject.format(duration, extra, matchPeriod);

        // Then
        assertThat(result)
            .isEqualTo("10:54 +24:38 - FIRST_HALF");
    }
}
