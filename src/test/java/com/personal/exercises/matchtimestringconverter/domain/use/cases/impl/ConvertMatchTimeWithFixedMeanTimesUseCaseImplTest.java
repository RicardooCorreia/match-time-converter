package com.personal.exercises.matchtimestringconverter.domain.use.cases.impl;

import com.personal.exercises.matchtimestringconverter.domain.catalog.MatchInput;
import com.personal.exercises.matchtimestringconverter.domain.catalog.MatchPeriodEnum;
import com.personal.exercises.matchtimestringconverter.domain.catalog.exceptions.InvalidTimeForPeriodException;
import com.personal.exercises.matchtimestringconverter.domain.extractors.MatchTimeInputStringExtractor;
import com.personal.exercises.matchtimestringconverter.domain.formatters.OutputMatchTimeFormatter;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

class ConvertMatchTimeWithFixedMeanTimesUseCaseImplTest {

    @Mock
    private MatchTimeInputStringExtractor matchTimeInputStringExtractor;

    @Mock
    private OutputMatchTimeFormatter outputMatchTimeFormatter;

    @Captor
    private ArgumentCaptor<Duration> durationArgumentCaptor;

    private ConvertMatchTimeWithFixedMeanTimesUseCaseImpl subject;

    @BeforeEach
    void setUp() {

        openMocks(this);
        subject = new ConvertMatchTimeWithFixedMeanTimesUseCaseImpl(matchTimeInputStringExtractor, outputMatchTimeFormatter);
    }

    @Test
    void convert_whenItsPreMatchAndIsZeroMinutes_returnOutput() {

        // Given
        final String inputString = "[PM] 0:00.000";

        final MatchInput matchInput = MatchInput.of(MatchPeriodEnum.PM, Duration.ZERO);

        doReturn(matchInput)
            .when(matchTimeInputStringExtractor)
            .extract(eq(inputString));

        final String formattedString = "00:00 – PRE_MATCH";
        doReturn(formattedString)
            .when(outputMatchTimeFormatter)
            .format(eq(matchInput.getTime()), eq(matchInput.getPeriod()));

        // When
        final String result = subject.convert(inputString);

        // Then
        assertThat(result)
            .isEqualTo(formattedString);
    }

    @Test
    void convert_whenItsPreMatchAndDoesNotHaveZeroMinutes_throwException() {

        // Given
        final String inputString = "[PM] 0:10.001";

        final MatchInput matchInput = MatchInput.of(MatchPeriodEnum.PM, Duration.ofMinutes(10).plusMillis(1));

        doReturn(matchInput)
            .when(matchTimeInputStringExtractor)
            .extract(eq(inputString));

        // When
        final ThrowableAssert.ThrowingCallable callable = () -> subject.convert(inputString);

        // Then
        assertThatThrownBy(callable)
            .isInstanceOf(InvalidTimeForPeriodException.class)
            .hasMessageContaining("Given time {10:00.001} is invalid, it should always be {00:00.000} for requested period {PRE_MATCH}");
    }

    @Test
    void convert_whenItsHalfTimeAndIts45Minutes_returnOutput() {

        // Given
        final String inputString = "[HT] 45:00.000";

        final MatchInput matchInput = MatchInput.of(MatchPeriodEnum.HT, Duration.ofMinutes(45));

        doReturn(matchInput)
            .when(matchTimeInputStringExtractor)
            .extract(eq(inputString));

        final String formattedString = "45:00 – HALF_TIME";
        doReturn(formattedString)
            .when(outputMatchTimeFormatter)
            .format(eq(matchInput.getTime()), eq(matchInput.getPeriod()));

        // When
        final String result = subject.convert(inputString);

        // Then
        assertThat(result)
            .isEqualTo(formattedString);
    }

    @Test
    void convert_whenItsHalfTimeAndItsNot45Minutes_throwException() {

        // Given
        final String inputString = "[HT] 0:36.066";

        final MatchInput matchInput = MatchInput.of(MatchPeriodEnum.HT, Duration.ofMinutes(36).plusMillis(66));

        doReturn(matchInput)
            .when(matchTimeInputStringExtractor)
            .extract(eq(inputString));

        // When
        final ThrowableAssert.ThrowingCallable callable = () -> subject.convert(inputString);

        // Then
        assertThatThrownBy(callable)
            .isInstanceOf(InvalidTimeForPeriodException.class)
            .hasMessageContaining("Given time {36:00.066} is invalid, it should always be {45:00.000} for requested period {HALF_TIME}");
    }

    @Test
    void convert_whenItsFullTimeAndIts90Minutes_returnOutput() {

        // Given
        final String inputString = "[FT] 90:00.000";

        final MatchInput matchInput = MatchInput.of(MatchPeriodEnum.FT, Duration.ofMinutes(90));

        doReturn(matchInput)
            .when(matchTimeInputStringExtractor)
            .extract(eq(inputString));

        final String formattedString = "90:00 – FULL_TIME";
        doReturn(formattedString)
            .when(outputMatchTimeFormatter)
            .format(eq(matchInput.getTime()), eq(matchInput.getPeriod()));

        // When
        final String result = subject.convert(inputString);

        // Then
        assertThat(result)
            .isEqualTo(formattedString);
    }

    @Test
    void convert_whenIsHalfTimeAndItsNot45Minutes_throwException() {

        // Given
        final String inputString = "[FT] 78:32.988";

        final MatchInput matchInput = MatchInput.of(MatchPeriodEnum.FT, Duration.ofMinutes(78).plusSeconds(32).plusMillis(988));

        doReturn(matchInput)
            .when(matchTimeInputStringExtractor)
            .extract(eq(inputString));

        // When
        final ThrowableAssert.ThrowingCallable callable = () -> subject.convert(inputString);

        // Then
        assertThatThrownBy(callable)
            .isInstanceOf(InvalidTimeForPeriodException.class)
            .hasMessageContaining("Given time {78:32.988} is invalid, it should always be {90:00.000} for requested period {FULL_TIME}");
    }

    @Test
    void convert_whenItsFirstHalfIsBefore45Minutes_truncateDurationAndReturnOutput() {

        // Given
        final String inputString = "[H1] 35:29.340";

        final MatchInput matchInput = MatchInput.of(
            MatchPeriodEnum.H1,
            Duration
                .ofMinutes(35)
                .plusSeconds(29)
                .plusMillis(340));

        doReturn(matchInput)
            .when(matchTimeInputStringExtractor)
            .extract(eq(inputString));

        final String formattedString = "35:29 – FIRST_HALF";
        doReturn(formattedString)
            .when(outputMatchTimeFormatter)
            .format(durationArgumentCaptor.capture(), eq(matchInput.getPeriod()));

        // When
        final String result = subject.convert(inputString);

        // Then
        assertThat(result)
            .isEqualTo(formattedString);

        final Duration duration = durationArgumentCaptor.getValue();
        assertThat(duration.toMinutes()).isEqualTo(35);
        assertThat(duration.toSecondsPart()).isEqualTo(29);
        assertThat(duration.toMillisPart()).isEqualTo(0);
    }

    @Test
    void convert_whenItsFirstHalfIsBefore45MinutesAndMoreThanHalfSecond_truncateDurationRoundingUpAndReturnOutput() {

        // Given
        final String inputString = "[H1] 12:45.667";

        final MatchInput matchInput = MatchInput.of(
            MatchPeriodEnum.H1,
            Duration
                .ofMinutes(12)
                .plusSeconds(45)
                .plusMillis(667));

        doReturn(matchInput)
            .when(matchTimeInputStringExtractor)
            .extract(eq(inputString));

        final String formattedString = "12:46 – FIRST_HALF";
        doReturn(formattedString)
            .when(outputMatchTimeFormatter)
            .format(durationArgumentCaptor.capture(), eq(matchInput.getPeriod()));

        // When
        final String result = subject.convert(inputString);

        // Then
        assertThat(result)
            .isEqualTo(formattedString);

        final Duration duration = durationArgumentCaptor.getValue();
        assertThat(duration.toMinutes()).isEqualTo(12);
        assertThat(duration.toSecondsPart()).isEqualTo(46);
        assertThat(duration.toMillisPart()).isEqualTo(0);
    }

    @Test
    void convert_whenItsFirstHalfIsAfter45Minutes_returnOutputWithExtra() {

        // Given
        final String inputString = "[H1] 46:21.340";

        final MatchInput matchInput = MatchInput.of(
            MatchPeriodEnum.H1,
            Duration
                .ofMinutes(46)
                .plusSeconds(21)
                .plusMillis(340));

        doReturn(matchInput)
            .when(matchTimeInputStringExtractor)
            .extract(eq(inputString));

        final String formattedString = "45:00 +01:21 – FIRST_HALF";
        doReturn(formattedString)
            .when(outputMatchTimeFormatter)
            .format(eq(Duration.ofMinutes(45)), durationArgumentCaptor.capture(), eq(matchInput.getPeriod()));

        // When
        final String result = subject.convert(inputString);

        // Then
        assertThat(result)
            .isEqualTo(formattedString);

        final Duration duration = durationArgumentCaptor.getValue();
        assertThat(duration.toMinutes()).isEqualTo(1);
        assertThat(duration.toSecondsPart()).isEqualTo(21);
        assertThat(duration.toMillisPart()).isEqualTo(0);
    }

    @Test
    void convert_whenItsFirstHalfIsAfter45MinutesAndMoreThanHalfSecond_returnOutputWithExtraRoundingUp() {

        // Given
        final String inputString = "[H1] 46:21.778";

        final MatchInput matchInput = MatchInput.of(
            MatchPeriodEnum.H1,
            Duration
                .ofMinutes(46)
                .plusSeconds(21)
                .plusMillis(778));

        doReturn(matchInput)
            .when(matchTimeInputStringExtractor)
            .extract(eq(inputString));

        final String formattedString = "45:00 +01:22 – FIRST_HALF";
        doReturn(formattedString)
            .when(outputMatchTimeFormatter)
            .format(eq(Duration.ofMinutes(45)), durationArgumentCaptor.capture(), eq(matchInput.getPeriod()));

        // When
        final String result = subject.convert(inputString);

        // Then
        assertThat(result)
            .isEqualTo(formattedString);

        final Duration duration = durationArgumentCaptor.getValue();
        assertThat(duration.toMinutes()).isEqualTo(1);
        assertThat(duration.toSecondsPart()).isEqualTo(22);
        assertThat(duration.toMillisPart()).isEqualTo(0);
    }

    @Test
    void convert_whenItsFirstHalfIs45Minutes_returnOutput() {

        // Given
        final String inputString = "[H1] 45:00.000";

        final MatchInput matchInput = MatchInput.of(
            MatchPeriodEnum.H1,
            Duration.ofMinutes(45));

        doReturn(matchInput)
            .when(matchTimeInputStringExtractor)
            .extract(eq(inputString));

        final String formattedString = "45:00 – FIRST_HALF";
        doReturn(formattedString)
            .when(outputMatchTimeFormatter)
            .format(durationArgumentCaptor.capture(), eq(matchInput.getPeriod()));

        // When
        final String result = subject.convert(inputString);

        // Then
        assertThat(result)
            .isEqualTo(formattedString);

        final Duration duration = durationArgumentCaptor.getValue();
        assertThat(duration.toMinutes()).isEqualTo(45);
        assertThat(duration.toSecondsPart()).isEqualTo(0);
        assertThat(duration.toMillisPart()).isEqualTo(0);
    }

    @Test
    void convert_whenItsSecondHalfIsBefore90Minutes_truncateDurationAndReturnOutput() {

        // Given
        final String inputString = "[H2] 82:56.124";

        final MatchInput matchInput = MatchInput.of(
            MatchPeriodEnum.H2,
            Duration
                .ofMinutes(82)
                .plusSeconds(56)
                .plusMillis(124));

        doReturn(matchInput)
            .when(matchTimeInputStringExtractor)
            .extract(eq(inputString));

        final String formattedString = "82:56 – SECOND_HALF";
        doReturn(formattedString)
            .when(outputMatchTimeFormatter)
            .format(durationArgumentCaptor.capture(), eq(matchInput.getPeriod()));

        // When
        final String result = subject.convert(inputString);

        // Then
        assertThat(result)
            .isEqualTo(formattedString);

        final Duration duration = durationArgumentCaptor.getValue();
        assertThat(duration.toMinutes()).isEqualTo(82);
        assertThat(duration.toSecondsPart()).isEqualTo(56);
        assertThat(duration.toMillisPart()).isEqualTo(0);
    }

    @Test
    void convert_whenItsSecondHalfIsBefore90MinutesAndMoreThanHalfSecond_truncateDurationRoundingUpAndReturnOutput() {

        // Given
        final String inputString = "[H2] 78:28.928";

        final MatchInput matchInput = MatchInput.of(
            MatchPeriodEnum.H2,
            Duration
                .ofMinutes(78)
                .plusSeconds(28)
                .plusMillis(928));

        doReturn(matchInput)
            .when(matchTimeInputStringExtractor)
            .extract(eq(inputString));

        final String formattedString = "78:28 – SECOND_HALF";
        doReturn(formattedString)
            .when(outputMatchTimeFormatter)
            .format(durationArgumentCaptor.capture(), eq(matchInput.getPeriod()));

        // When
        final String result = subject.convert(inputString);

        // Then
        assertThat(result)
            .isEqualTo(formattedString);

        final Duration duration = durationArgumentCaptor.getValue();
        assertThat(duration.toMinutes()).isEqualTo(78);
        assertThat(duration.toSecondsPart()).isEqualTo(29);
        assertThat(duration.toMillisPart()).isEqualTo(0);
    }

    @Test
    void convert_whenItsSecondHalfIsAfter90Minutes_returnOutputWithExtra() {

        // Given
        final String inputString = "[H2] 98:27.367";

        final MatchInput matchInput = MatchInput.of(
            MatchPeriodEnum.H2,
            Duration
                .ofMinutes(98)
                .plusSeconds(27)
                .plusMillis(367));

        doReturn(matchInput)
            .when(matchTimeInputStringExtractor)
            .extract(eq(inputString));

        final String formattedString = "90:00 +08:27 – SECOND_HALF";
        doReturn(formattedString)
            .when(outputMatchTimeFormatter)
            .format(eq(Duration.ofMinutes(90)), durationArgumentCaptor.capture(), eq(matchInput.getPeriod()));

        // When
        final String result = subject.convert(inputString);

        // Then
        assertThat(result)
            .isEqualTo(formattedString);

        final Duration duration = durationArgumentCaptor.getValue();
        assertThat(duration.toMinutes()).isEqualTo(8);
        assertThat(duration.toSecondsPart()).isEqualTo(27);
        assertThat(duration.toMillisPart()).isEqualTo(0);
    }

    @Test
    void convert_whenItsSecondHalfIsAfter90MinutesAndMoreThanHalfSecond_returnOutputWithExtraRoundingUp() {

        // Given
        final String inputString = "[H2] 101:59.778";

        final MatchInput matchInput = MatchInput.of(
            MatchPeriodEnum.H2,
            Duration
                .ofMinutes(101)
                .plusSeconds(59)
                .plusMillis(778));

        doReturn(matchInput)
            .when(matchTimeInputStringExtractor)
            .extract(eq(inputString));

        final String formattedString = "90:00 +12:00 – SECOND_HALF";
        doReturn(formattedString)
            .when(outputMatchTimeFormatter)
            .format(eq(Duration.ofMinutes(90)), durationArgumentCaptor.capture(), eq(matchInput.getPeriod()));

        // When
        final String result = subject.convert(inputString);

        // Then
        assertThat(result)
            .isEqualTo(formattedString);

        final Duration duration = durationArgumentCaptor.getValue();
        assertThat(duration.toMinutes()).isEqualTo(12);
        assertThat(duration.toSecondsPart()).isEqualTo(0);
        assertThat(duration.toMillisPart()).isEqualTo(0);
    }

    @Test
    void convert_whenItsSecondHalfIs90Minutes_returnOutput() {

        // Given
        final String inputString = "[H2] 90:00.000";

        final MatchInput matchInput = MatchInput.of(
            MatchPeriodEnum.H2,
            Duration.ofMinutes(90));

        doReturn(matchInput)
            .when(matchTimeInputStringExtractor)
            .extract(eq(inputString));

        final String formattedString = "90:00 – SECOND_HALF";
        doReturn(formattedString)
            .when(outputMatchTimeFormatter)
            .format(durationArgumentCaptor.capture(), eq(matchInput.getPeriod()));

        // When
        final String result = subject.convert(inputString);

        // Then
        assertThat(result)
            .isEqualTo(formattedString);

        final Duration duration = durationArgumentCaptor.getValue();
        assertThat(duration.toMinutes()).isEqualTo(90);
        assertThat(duration.toSecondsPart()).isEqualTo(0);
        assertThat(duration.toMillisPart()).isEqualTo(0);
    }

    @Test
    void convert_whenItsFirstHalfButIsBefore0Minutes_throwException() {

        // Given
        final String inputString = "[H1] -12:12.092";

        final MatchInput matchInput = MatchInput.of(
            MatchPeriodEnum.H2,
            Duration.ofMinutes(-12)
                .plusSeconds(12)
                .plusMillis(92));

        doReturn(matchInput)
            .when(matchTimeInputStringExtractor)
            .extract(eq(inputString));

        // When
        final ThrowableAssert.ThrowingCallable callable = () -> subject.convert(inputString);

        // Then
        assertThatThrownBy(callable)
            .isInstanceOf(InvalidTimeForPeriodException.class)
            .hasMessageContaining("Given time {-11:-48.092} is before minimum time {45:00.000} for requested period {SECOND_HALF}");
    }

    @Test
    void convert_whenItsSecondHalfButIsBefore45Minutes_throwException() {

        // Given
        final String inputString = "[H2] 43:12.092";

        final MatchInput matchInput = MatchInput.of(
            MatchPeriodEnum.H2,
            Duration.ofMinutes(43)
                .plusSeconds(12)
                .plusMillis(92));

        doReturn(matchInput)
            .when(matchTimeInputStringExtractor)
            .extract(eq(inputString));

        // When
        final ThrowableAssert.ThrowingCallable callable = () -> subject.convert(inputString);

        // Then
        assertThatThrownBy(callable)
            .isInstanceOf(InvalidTimeForPeriodException.class)
            .hasMessageContaining("Given time {43:12.092} is before minimum time {45:00.000} for requested period {SECOND_HALF}");
    }
}
