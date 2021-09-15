package com.personal.exercises.matchtimestringconverter.domain.catalog;

import java.time.Duration;
import java.util.Objects;

public interface MatchPeriod {

    String getShortForm();

    String getLongForm();

    Duration getInitialTime();

    Duration getFinalTime();

    default boolean isMeanTime() {

        return Objects.equals(getInitialTime(), getFinalTime());
    }
}
