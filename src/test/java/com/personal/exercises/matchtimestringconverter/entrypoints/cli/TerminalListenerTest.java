package com.personal.exercises.matchtimestringconverter.entrypoints.cli;

import com.personal.exercises.matchtimestringconverter.domain.catalog.exceptions.InvalidInputException;
import com.personal.exercises.matchtimestringconverter.domain.use.cases.ConvertMatchTimeUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.openMocks;

class TerminalListenerTest {

    @Mock
    private ConvertMatchTimeUseCase convertMatchTimeUseCase;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private TerminalListener subject;

    private PrintStream out;

    private InputStream in;

    private PrintStream err;


    @BeforeEach
    void setUp() {

        openMocks(this);
        subject = new TerminalListener(convertMatchTimeUseCase, executorService);
        in = System.in;
        out = System.out;
        err = System.err;
    }

    @Test
    void listen_whenValid_presentOutput() {

        // Given
        final String inputData = "Input date";
        System.setIn(new ByteArrayInputStream(inputData.getBytes(Charset.defaultCharset())));

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final PrintStream printStream = new PrintStream(outputStream, false, Charset.defaultCharset());
        System.setOut(printStream);

        final String output = "Output data";
        doReturn(output)
            .when(convertMatchTimeUseCase)
            .convert(eq(inputData));

        // When
        subject.listen();

        // Then
        await()
            .atMost(500, TimeUnit.MILLISECONDS)
            .untilAsserted(() -> {
                final String data = new String(outputStream.toByteArray(), Charset.defaultCharset());
                assertThat(data)
                    .contains("Output data");
            });
    }

    @Test
    void listen_whenInvalidInputExceptionIsThrown_presentInvalid() {

        // Given
        final String inputData = "Input date";
        System.setIn(new ByteArrayInputStream(inputData.getBytes(Charset.defaultCharset())));

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final PrintStream printStream = new PrintStream(outputStream, false, Charset.defaultCharset());
        System.setErr(printStream);

        final InvalidInputException invalidInputException = mock(InvalidInputException.class);
        final String message = "Reason to be invalid";
        doReturn(message)
            .when(invalidInputException)
            .getMessage();
        doThrow(invalidInputException)
            .when(convertMatchTimeUseCase)
            .convert(eq(inputData));

        // When
        subject.listen();

        // Then
        await()
            .atMost(500, TimeUnit.MILLISECONDS)
            .untilAsserted(() -> {
                final String data = new String(outputStream.toByteArray(), Charset.defaultCharset());
                assertThat(data)
                    .contains("INVALID - " + message);
            });
    }

    @Test
    void listen_whenExceptionIsThrown_presentError() {

        // Given
        final String inputData = "Input date";
        System.setIn(new ByteArrayInputStream(inputData.getBytes(Charset.defaultCharset())));

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final PrintStream printStream = new PrintStream(outputStream, false, Charset.defaultCharset());
        System.setErr(printStream);

        final String message = "Reason to be invalid";
        final RuntimeException exception = new RuntimeException(message);
        doThrow(exception)
            .when(convertMatchTimeUseCase)
            .convert(eq(inputData));

        // When
        subject.listen();

        // Then
        await()
            .atMost(500, TimeUnit.MILLISECONDS)
            .untilAsserted(() -> {
                final String data = new String(outputStream.toByteArray(), Charset.defaultCharset());
                assertThat(data)
                    .contains("ERROR - Something went wrong: " + message);
            });
    }

    @AfterEach
    void tearDown() {

        System.setIn(in);
        System.setOut(out);
        System.setErr(err);
    }
}
