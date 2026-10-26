package com.polyglotmesh.engine;

import org.springframework.stereotype.Service;

@Service
public class ExecutionService {

    public ExecutionResult execute(String language, String code) {

    long startTime = System.currentTimeMillis();

    if (code == null || code.isBlank()) {
        return new ExecutionResult(
                false,
                "",
                "Code cannot be empty",
                System.currentTimeMillis() - startTime
        );
    }

    try {
        LanguageExecutor executor =
                ExecutorFactory.getExecutor(language);

        return executor.execute(code);

        } catch (Exception e) {

            long executionTime =
                    System.currentTimeMillis() - startTime;

            return new ExecutionResult(
                    false,
                    "",
                    e.getMessage(),
                    executionTime
            );
        }
    }
}