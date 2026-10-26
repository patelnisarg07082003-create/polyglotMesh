package com.polyglotmesh.engine;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.graalvm.polyglot.Context;

public class JavaScriptExecutor implements LanguageExecutor {

    private static final long TIMEOUT_SECONDS = 5;

    @Override
    public ExecutionResult execute(String code) {

        long startTime = System.currentTimeMillis();

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        ExecutorService executorService =
                Executors.newSingleThreadExecutor();

        Context context = null;

        try {
            context = Context.newBuilder("js")
                    .out(new PrintStream(outputStream))
                    .build();

            Context finalContext = context;

Future<Void> future = executorService.submit(() -> {

    finalContext.eval("js", code);

    return null;
});

try {

    future.get(
            TIMEOUT_SECONDS,
            TimeUnit.SECONDS
    );

    long executionTime =
            System.currentTimeMillis() - startTime;

    return new ExecutionResult(
            true,
            outputStream.toString(),
            null,
            executionTime
    );

} catch (InterruptedException e) {

    Thread.currentThread().interrupt();

    return new ExecutionResult(
            false,
            outputStream.toString(),
            "JavaScript execution was interrupted",
            System.currentTimeMillis() - startTime
    );

} catch (TimeoutException e) {

    future.cancel(true);

    try {
        context.close(true);
    } catch (Exception ignored) {
    }

    long executionTime =
            System.currentTimeMillis() - startTime;

    return new ExecutionResult(
            false,
            outputStream.toString(),
            "Execution timed out after "
                    + TIMEOUT_SECONDS
                    + " seconds",
            executionTime
    );
}

        } catch (ExecutionException e) {

            Throwable cause = e.getCause();

            String errorMessage =
                    cause != null && cause.getMessage() != null
                            ? cause.getMessage()
                            : "JavaScript execution failed";

            return new ExecutionResult(
                    false,
                    outputStream.toString(),
                    errorMessage,
                    System.currentTimeMillis() - startTime
            );

        } catch (Exception e) {

            return new ExecutionResult(
                    false,
                    outputStream.toString(),
                    e.getMessage() != null
                            ? e.getMessage()
                            : "JavaScript execution failed",
                    System.currentTimeMillis() - startTime
            );

        } finally {

            if (context != null) {
                try {
                    context.close(true);
                } catch (Exception ignored) {
                }
            }

            executorService.shutdownNow();
        }
    }
}