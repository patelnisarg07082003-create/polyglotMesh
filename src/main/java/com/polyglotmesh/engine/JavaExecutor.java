package com.polyglotmesh.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class JavaExecutor implements LanguageExecutor {

    private static final long TIMEOUT_SECONDS = 5;

    @Override
    public ExecutionResult execute(String code) {

        long startTime = System.currentTimeMillis();

        Path tempDir = null;

        try {
            tempDir = Files.createTempDirectory("polyglot-java-");

            Path javaFile = tempDir.resolve("Main.java");

            Files.writeString(
                    javaFile,
                    code,
                    StandardCharsets.UTF_8
            );

            // Compile Java source code
            Process compileProcess = new ProcessBuilder(
                    getJavaCompiler(),
                    javaFile.toString()
            )
                    .directory(tempDir.toFile())
                    .redirectErrorStream(true)
                    .start();

            String compileOutput = readOutput(
                    compileProcess.getInputStream()
            );

            boolean compiled = compileProcess.waitFor(
                    TIMEOUT_SECONDS,
                    TimeUnit.SECONDS
            );

            if (!compiled) {
                compileProcess.destroyForcibly();

                return new ExecutionResult(
                        false,
                        "",
                        "Compilation timed out",
                        System.currentTimeMillis() - startTime
                );
            }

            if (compileProcess.exitValue() != 0) {

                return new ExecutionResult(
                        false,
                        "",
                        compileOutput,
                        System.currentTimeMillis() - startTime
                );
            }

            // Run compiled Java program
Process runProcess = new ProcessBuilder(
        getJavaCommand(),
        "-cp",
        tempDir.toString(),
        "Main"
)
        .directory(tempDir.toFile())
        .redirectErrorStream(true)
        .start();

// Wait for the process with timeout BEFORE reading the output.
// This ensures a long-running program is actually terminated.
boolean finished = runProcess.waitFor(
        TIMEOUT_SECONDS,
        TimeUnit.SECONDS
);

if (!finished) {

    runProcess.destroyForcibly();

    // Wait briefly for the process to terminate completely.
    runProcess.waitFor();

    return new ExecutionResult(
            false,
            "",
            "Execution timed out",
            System.currentTimeMillis() - startTime
    );
}

// Process finished within the timeout, so now it is safe to read its output.
String output = readOutput(
        runProcess.getInputStream()
);

if (runProcess.exitValue() != 0) {

    return new ExecutionResult(
            false,
            output,
            "Java program exited with code: "
                    + runProcess.exitValue(),
            System.currentTimeMillis() - startTime
    );
}

            return new ExecutionResult(
                    true,
                    output,
                    null,
                    System.currentTimeMillis() - startTime
            );

        } catch (Exception e) {

            return new ExecutionResult(
                    false,
                    "",
                    e.getMessage(),
                    System.currentTimeMillis() - startTime
            );

        } finally {

            if (tempDir != null) {
                deleteDirectory(tempDir.toFile());
            }
        }
    }

    private String readOutput(InputStream inputStream)
            throws Exception {

        StringBuilder output = new StringBuilder();

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     inputStream,
                                     StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }
        }

        return output.toString();
    }

    private String getJavaCompiler() {

        String javaHome = System.getProperty("java.home");

        File compiler = new File(
                javaHome,
                "bin/javac"
        );

        if (compiler.exists()) {
            return compiler.getAbsolutePath();
        }

        return "javac";
    }

    private String getJavaCommand() {

        String javaHome = System.getProperty("java.home");

        File java = new File(
                javaHome,
                "bin/java"
        );

        if (java.exists()) {
            return java.getAbsolutePath();
        }

        return "java";
    }

    private void deleteDirectory(File directory) {

        if (!directory.exists()) {
            return;
        }

        File[] files = directory.listFiles();

        if (files != null) {

            for (File file : files) {

                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }

        directory.delete();
    }
}