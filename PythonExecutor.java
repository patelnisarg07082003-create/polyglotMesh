package com.polyglotmesh.engine;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class PythonExecutor implements LanguageExecutor {

    private static final long TIMEOUT_SECONDS = 5;

    @Override
    public ExecutionResult execute(String code) {

        long startTime = System.currentTimeMillis();

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        Context context = Context.newBuilder("python")
                .out(new PrintStream(outputStream))
                .build();

        ExecutorService executor =
                Executors.newSingleThreadExecutor();

        try {

            Future<?> future = executor.submit(() ->
                    context.eval("python", code)
            );

            try {

                future.get(
                        TIMEOUT_SECONDS,
                        TimeUnit.SECONDS
                );

                return new ExecutionResult(
                        true,
                        outputStream.toString(),
                        null,
                        System.currentTimeMillis() - startTime
                );

            } catch (java.util.concurrent.TimeoutException e) {

                future.cancel(true);
                context.close(true);

                return new ExecutionResult(
                        false,
                        outputStream.toString(),
                        "Execution timed out",
                        System.currentTimeMillis() - startTime
                );

            } catch (Exception e) {

                return new ExecutionResult(
                        false,
                        outputStream.toString(),
                        e.getMessage(),
                        System.currentTimeMillis() - startTime
                );
            }

        } finally {

            try {
                context.close();
            } catch (Exception ignored) {
            }

            executor.shutdownNow();
        }
    }


    // =========================================================
    // Java → Python Interoperability
    // =========================================================

    public Object executeWithJavaValue(
            String pythonCode,
            String variableName,
            Object javaValue) {

        try {

            // Validate variable name
            if (variableName == null
                    || variableName.isBlank()
                    || !variableName.matches("[A-Za-z_][A-Za-z0-9_]*")
                    || isPythonKeyword(variableName)) {

                throw new IllegalArgumentException(
                        "Invalid Python variable name"
                );
            }

            // Validate Python code
            if (pythonCode == null || pythonCode.isBlank()) {

                throw new IllegalArgumentException(
                        "Python code cannot be empty"
                );
            }

            ByteArrayOutputStream outputStream =
                    new ByteArrayOutputStream();

            try (Context context = Context.newBuilder("python")
                    .out(new PrintStream(outputStream))
                    .allowAllAccess(false)
                    .build()) {

                Value bindings =
                        context.getBindings("python");

                /*
                 * Java Map → Python Dictionary
                 */
                if (javaValue instanceof Map<?, ?> map) {

                    String pythonDictionary =
                            convertMapToPythonDictionary(map);

                    String setupCode =
                            variableName
                                    + " = "
                                    + pythonDictionary;

                    context.eval(
                            "python",
                            setupCode
                    );
                }

                /*
                 * Java List → Python List
                 */
                else if (javaValue instanceof java.util.List<?> list) {

                    String pythonList =
                            convertListToPythonList(list);

                    String setupCode =
                            variableName
                                    + " = "
                                    + pythonList;

                    context.eval(
                            "python",
                            setupCode
                    );
                }

                /*
                 * Simple Java Value
                 */
                else {

                    bindings.putMember(
                            variableName,
                            javaValue
                    );
                }


                // Execute Python code
                Value result =
                        context.eval(
                                "python",
                                pythonCode
                        );


                // Get printed output
                String output =
                        outputStream.toString();

                if (!output.isBlank()) {

                    return output.trim();
                }


                // Return Python result
                if (result.isNumber()) {

                    return result.asDouble();
                }

                if (result.isString()) {

                    return result.asString();
                }

                if (result.isBoolean()) {

                    return result.asBoolean();
                }

                return result.toString();
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Python interoperability failed: "
                            + e.getMessage(),
                    e
            );
        }
    }


    // =========================================================
    // Interoperability + Timeout + Metrics
    // =========================================================


    public ExecutionResult executeInteropWithMetrics(
            String pythonCode,
            String variableName,
            Object javaValue) {

        long startTime = System.currentTimeMillis();

        ExecutorService executor =
                Executors.newSingleThreadExecutor();

        Future<Object> future = null;

        try {

            future = executor.submit(() ->
                    executeWithJavaValue(
                            pythonCode,
                            variableName,
                            javaValue
                    )
            );

            Object result = future.get(
                    TIMEOUT_SECONDS,
                    TimeUnit.SECONDS
            );

            return new ExecutionResult(
                    true,
                    String.valueOf(result),
                    null,
                    System.currentTimeMillis() - startTime
            );

        } catch (java.util.concurrent.TimeoutException e) {

            if (future != null) {
                future.cancel(true);
            }

            return new ExecutionResult(
                    false,
                    null,
                    "Execution timed out",
                    System.currentTimeMillis() - startTime
            );

        } catch (Exception e) {

            return new ExecutionResult(
                    false,
                    null,
                    e.getMessage(),
                    System.currentTimeMillis() - startTime
            );

        } finally {

            executor.shutdownNow();
        }
    }




    // =========================================================
    // Helper: Java Map → Python Dictionary
    // =========================================================

    private String convertMapToPythonDictionary(
            Map<?, ?> map) {

        StringBuilder builder =
                new StringBuilder();

        builder.append("{");

        boolean first = true;

        for (Map.Entry<?, ?> entry :
                map.entrySet()) {

            if (!first) {

                builder.append(",");
            }

            String key =
                    String.valueOf(entry.getKey());

            Object value =
                    entry.getValue();

            builder.append("'");

            builder.append(
                    escapePythonString(key)
            );

            builder.append("':");

            builder.append(
                    convertJavaValueToPythonLiteral(value)
            );

            first = false;
        }

        builder.append("}");

        return builder.toString();
    }


    // =========================================================
    // Helper: Java List → Python List
    // =========================================================

    private String convertListToPythonList(
            java.util.List<?> list) {

        StringBuilder builder =
                new StringBuilder();

        builder.append("[");

        boolean first = true;

        for (Object item : list) {

            if (!first) {

                builder.append(",");
            }

            builder.append(
                    convertJavaValueToPythonLiteral(item)
            );

            first = false;
        }

        builder.append("]");

        return builder.toString();
    }


    // =========================================================
    // Helper: Java Object → Python Literal
    // =========================================================

    private String convertJavaValueToPythonLiteral(
            Object value) {

        if (value == null) {

            return "None";
        }

        if (value instanceof String string) {

            return "'"
                    + escapePythonString(string)
                    + "'";
        }

        if (value instanceof Boolean bool) {

            return bool ? "True" : "False";
        }

        if (value instanceof Number number) {

            return number.toString();
        }

        if (value instanceof Map<?, ?> map) {

            return convertMapToPythonDictionary(map);
        }

        if (value instanceof java.util.List<?> list) {

            return convertListToPythonList(list);
        }

        return "'"
                + escapePythonString(
                String.valueOf(value)
        )
                + "'";
    }


    // =========================================================
    // Helper: Escape Python String
    // =========================================================

    private String escapePythonString(
            String value) {

        return value
                .replace("\\", "\\\\")
                .replace("'", "\\'")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    private boolean isPythonKeyword(String variableName) {

        return switch (variableName) {
            case "False", "None", "True",
                 "and", "as", "assert", "async", "await",
                 "break", "case", "class", "continue",
                 "def", "del", "elif", "else", "except",
                 "finally", "for", "from", "global", "if",
                 "import", "in", "is", "lambda", "match",
                 "nonlocal", "not", "or", "pass", "raise",
                 "return", "try", "while", "with", "yield" -> true;

            default -> false;
        };
    }
}

