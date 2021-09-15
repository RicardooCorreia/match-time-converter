package com.personal.exercises.matchtimestringconverter.domain.catalog;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum MatchPeriodEnum implements MatchPeriod {
    PM("PM", "PRE_MATCH", Duration.ofMinutes(0), Duration.ofMinutes(0)),
    H1("H1", "FIRST_HALF", Duration.ofMinutes(0), Duration.ofMinutes(45)),
    HT("HT", "HALF_TIME", Duration.ofMinutes(45), Duration.ofMinutes(45)),
    H2("H2", "SECOND_HALF", Duration.ofMinutes(45), Duration.ofMinutes(90)),
    FT("FT", "FULL_TIME", Duration.ofMinutes(90), Duration.ofMinutes(90));

    private final String shortForm;

    private final String longForm;

    private final Duration initialTime;

    private final Duration finalTime;

    public static Optional<MatchPeriodEnum> valueOfShortForm(String shortForm) {

        return Arrays.stream(MatchPeriodEnum.values())
            .filter(period -> period.shortForm.equals(shortForm))
            .findAny();
    }
}
