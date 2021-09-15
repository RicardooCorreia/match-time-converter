package com.personal.exercises.matchtimestringconverter.domain.extractors;

import com.personal.exercises.matchtimestringconverter.domain.catalog.MatchInput;
import com.personal.exercises.matchtimestringconverter.domain.catalog.MatchPeriodEnum;
import com.personal.exercises.matchtimestringconverter.domain.catalog.exceptions.InputStringDoesNotMatchPatternException;
import com.personal.exercises.matchtimestringconverter.domain.constants.RegexConstants;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MatchTimeInputStringExtractorTest {

    private MatchTimeInputStringExtractor subject = new MatchTimeInputStringExtractor();

    @Test
    void extract_whenStringIsValid_returnMatchTimeInput() {

        // Given
        final String inputString = "[H1] 34:23.123";

        // When
        final MatchInput result = subject.extract(inputString);

        // Then
        final MatchInput expected = MatchInput.of(MatchPeriodEnum.H1,
            Duration.ofMinutes(34)
                .plusSeconds(23)
                .plusMillis(123));
        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @Test
    void extract_whenStringIsAllZeros_returnMatchTimeInput() {

        // Given
        final String inputString = "[PM] 0:00.000";

        // When
        final MatchInput result = subject.extract(inputString);

        // Then
        final MatchInput expected = MatchInput.of(MatchPeriodEnum.PM,
            Duration.ofMinutes(0)
                .plusSeconds(0)
                .plusMillis(0));
        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @Test
    void extract_whenUpperLimit_returnMatchTimeInput() {

        // Given
        final String inputString = "[FT] 90:59.999";

        // When
        final MatchInput result = subject.extract(inputString);

        // Then
        final MatchInput expected = MatchInput.of(MatchPeriodEnum.FT,
            Duration.ofMinutes(90)
                .plusSeconds(59)
                .plusMillis(999));
        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @Test
    void extract_whenMinutesHaveThreeDigits_returnMatchTimeInput() {

        // Given
        final String inputString = "[FT] 120:59.999";

        // When
        final MatchInput result = subject.extract(inputString);

        // Then
        final MatchInput expected = MatchInput.of(MatchPeriodEnum.FT,
            Duration.ofMinutes(120)
                .plusSeconds(59)
                .plusMillis(999));
        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @Test
    void extract_whenNegativeMinutes_returnException() {

        // Given
        final String inputString = "[FT] -90:59.999";

        // When
        final ThrowableAssert.ThrowingCallable callable = () -> subject.extract(inputString);

        // Then
        assertThatThrownBy(callable)
            .isInstanceOf(InputStringDoesNotMatchPatternException.class)
            .hasMessageContaining(
                "Given string {[FT] -90:59.999} does not match required pattern"
                    + " {" + RegexConstants.MATCH_INPUT_REGEX + "}");
    }

    @Test
    void extract_whenNegativeSeconds_returnException() {

        // Given
        final String inputString = "[FT] 90:-59.999";

        // When
        final ThrowableAssert.ThrowingCallable callable = () -> subject.extract(inputString);

        // Then
        assertThatThrownBy(callable)
            .isInstanceOf(InputStringDoesNotMatchPatternException.class)
            .hasMessageContaining(
                "Given string {[FT] 90:-59.999} does not match required pattern"
                    + " {" + RegexConstants.MATCH_INPUT_REGEX + "}");
    }

    @Test
    void extract_whenNegativeMilliseconds_returnException() {

        // Given
        final String inputString = "[FT] 90:59.-999";

        // When
        final ThrowableAssert.ThrowingCallable callable = () -> subject.extract(inputString);

        // Then
        assertThatThrownBy(callable)
            .isInstanceOf(InputStringDoesNotMatchPatternException.class)
            .hasMessageContaining(
                "Given string {[FT] 90:59.-999} does not match required pattern"
                    + " {" + RegexConstants.MATCH_INPUT_REGEX + "}");
    }

    @Test
    void extract_withoutPeriod_returnException() {

        // Given
        final String inputString = "90:59.999";

        // When
        final ThrowableAssert.ThrowingCallable callable = () -> subject.extract(inputString);

        // Then
        assertThatThrownBy(callable)
            .isInstanceOf(InputStringDoesNotMatchPatternException.class)
            .hasMessageContaining(
                "Given string {90:59.999} does not match required pattern"
                    + " {" + RegexConstants.MATCH_INPUT_REGEX + "}");
    }

    @Test
    void extract_withInvalidString_returnException() {

        // Given
        final String inputString = "Invalid";

        // When
        final ThrowableAssert.ThrowingCallable callable = () -> subject.extract(inputString);

        // Then
        assertThatThrownBy(callable)
            .isInstanceOf(InputStringDoesNotMatchPatternException.class)
            .hasMessageContaining(
                "Given string {Invalid} does not match required pattern"
                    + " {" + RegexConstants.MATCH_INPUT_REGEX + "}");
    }
}
