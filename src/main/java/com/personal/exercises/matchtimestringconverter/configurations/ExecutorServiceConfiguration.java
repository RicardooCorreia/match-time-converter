package com.personal.exercises.matchtimestringconverter.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorServiceConfiguration {

    public static final int N_THREADS = 5;

    @Bean
    ExecutorService executorService() {

        return Executors.newFixedThreadPool(N_THREADS);
    }
}
