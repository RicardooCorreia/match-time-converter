package com.personal.exercises.matchtimestringconverter.entrypoints.rest.controllers;

import com.personal.exercises.matchtimestringconverter.domain.use.cases.ConvertMatchTimeUseCase;
import com.personal.exercises.matchtimestringconverter.entrypoints.rest.responses.MatchInputConversionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConvertMatchTimeController {

    private final ConvertMatchTimeUseCase convertMatchTimeUseCase;

    @GetMapping("/api/match-time/conversion")
    public MatchInputConversionResponse convertMatchTime(@RequestParam String inputString) {

        final String result = convertMatchTimeUseCase.convert(inputString);
        return MatchInputConversionResponse.of(result);
    }
}
