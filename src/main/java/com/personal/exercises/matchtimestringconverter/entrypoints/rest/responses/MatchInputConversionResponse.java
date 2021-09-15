package com.personal.exercises.matchtimestringconverter.entrypoints.rest.responses;

import lombok.Value;

@Value(staticConstructor = "of")
public class MatchInputConversionResponse {

    String result;
}
