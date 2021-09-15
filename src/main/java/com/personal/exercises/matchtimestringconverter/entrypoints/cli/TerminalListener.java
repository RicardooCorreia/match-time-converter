package com.personal.exercises.matchtimestringconverter.entrypoints.cli;

import com.personal.exercises.matchtimestringconverter.domain.catalog.exceptions.InvalidInputException;
import com.personal.exercises.matchtimestringconverter.domain.use.cases.ConvertMatchTimeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

@Component
@RequiredArgsConstructor
public class TerminalListener {

    private final ConvertMatchTimeUseCase convertMatchTimeUseCase;

    private final ExecutorService executorService;

    @PostConstruct
    public void listen() {

        executorService.submit(() -> {
            final Scanner in = new Scanner(System.in, Charset.defaultCharset());

            while (in.hasNext()) {

                handleInput(in.nextLine());
            }
        });
    }

    private void handleInput(String input) {

        try {
            final String result = convertMatchTimeUseCase.convert(input);
            System.out.println(result);
        } catch (InvalidInputException ex) {
            System.err.println("INVALID - " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("ERROR - Something went wrong: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
