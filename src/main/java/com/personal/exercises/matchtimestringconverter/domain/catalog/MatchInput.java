package com.personal.exercises.matchtimestringconverter.domain.catalog;

import lombok.Value;

import java.time.Duration;

@Value(staticConstructor = "of")
public class MatchInput {

    MatchPeriod period;

    Duration time;
}
