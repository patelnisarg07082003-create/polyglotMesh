package com.polyglotmesh.engine;

public class ExecutorFactory {

    public static LanguageExecutor getExecutor(String language) {

        if (language == null || language.isBlank()) {
            throw new IllegalArgumentException(
                    "Language cannot be null or empty"
            );
        }

        return switch (language.toLowerCase()) {

            case "python" ->
                    new PythonExecutor();

            case "javascript", "js" ->
                    new JavaScriptExecutor();

            case "java" ->
                    new JavaExecutor();

            default ->
                    throw new IllegalArgumentException(
                            "Unsupported language: " + language
                    );
        };
    }
}