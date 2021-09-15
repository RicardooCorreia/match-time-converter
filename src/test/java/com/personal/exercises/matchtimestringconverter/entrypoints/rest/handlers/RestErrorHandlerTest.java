package com.personal.exercises.matchtimestringconverter.entrypoints.rest.handlers;

import com.personal.exercises.matchtimestringconverter.domain.catalog.exceptions.ApplicationException;
import com.personal.exercises.matchtimestringconverter.domain.catalog.exceptions.InvalidInputException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = RestErrorHandler.class)
class RestErrorHandlerTest {

    @SpyBean
    private TestController testController;

    @Autowired
    private MockMvc mvc;

    @Test
    void perform_whenInvalidInputExceptionIsThrown_returnBadRequest() throws Exception {

        // Given
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/test-rest-error-handler");

        testController.setSupplier(() -> {
            throw new TestInvalidInputException("Invalid input");
        });

        // When
        final ResultActions result = mvc.perform(requestBuilder);

        // Then
        result
            .andExpect(status().isBadRequest())
            .andExpect(content().string("INVALID - Invalid input"));
    }

    @Test
    void perform_whenApplicationExceptionIsThrown_returnInternalError() throws Exception {

        // Given
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/test-rest-error-handler");

        testController.setSupplier(() -> {
            throw new TestApplicationException("Uncaught error");
        });

        // When
        final ResultActions result = mvc.perform(requestBuilder);

        // Then
        result
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("Application error - Uncaught error"));
    }

    @Test
    void perform_whenExceptionIsThrown_returnBadRequest() throws Exception {

        // Given
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/test-rest-error-handler");

        testController.setSupplier(() -> {
            throw new RuntimeException("Something is broken!");
        });

        // When
        final ResultActions result = mvc.perform(requestBuilder);

        // Then
        result
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("Unexpected error - Something is broken!"));
    }

    @RestController
    private static class TestController {

        private Supplier<Object> supplier;

        public void setSupplier(Supplier<Object> supplier) {

            this.supplier = supplier;
        }

        @GetMapping("/test-rest-error-handler")
        public void get() {

            supplier.get();
        }
    }

    private static class TestInvalidInputException extends InvalidInputException {

        TestInvalidInputException(String message) {

            super(message);
        }
    }

    private static class TestApplicationException extends ApplicationException {

        TestApplicationException(String message) {

            super(message);
        }
    }
}
