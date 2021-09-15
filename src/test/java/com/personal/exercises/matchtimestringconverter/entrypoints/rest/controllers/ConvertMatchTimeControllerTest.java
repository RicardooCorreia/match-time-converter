package com.personal.exercises.matchtimestringconverter.entrypoints.rest.controllers;

import com.personal.exercises.matchtimestringconverter.domain.use.cases.ConvertMatchTimeUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConvertMatchTimeController.class)
class ConvertMatchTimeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ConvertMatchTimeUseCase convertMatchTimeUseCase;

    @Test
    void convertMatchTime() throws Exception {

        // Given
        final String inputData = "[H1] 46:15.752";

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/match-time/conversion")
            .param("inputString", inputData);

        final String outputData = "45:00 +01:16 – FIRST_HALF";
        doReturn(outputData)
            .when(convertMatchTimeUseCase)
            .convert(eq(inputData));

        // When
        final ResultActions result = mvc.perform(requestBuilder);

        // Then
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result", is(outputData)));
    }
}
