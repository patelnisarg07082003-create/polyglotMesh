package com.polyglotmesh.engine;

import org.springframework.stereotype.Service;

@Service
public class DataExchangeService {

    public DataExchangeResult executeJavaToPython(
            String javaCode,
            String pythonCode) {

        long startTime = System.currentTimeMillis();

        try {

            // Step 1: Execute Java code
            JavaExecutor javaExecutor = new JavaExecutor();

            ExecutionResult javaResult =
                    javaExecutor.execute(javaCode);

            long javaExecutionTime =
                    javaResult.getExecutionTime();

            if (!javaResult.isSuccess()) {

                return new DataExchangeResult(
                        false,
                        javaResult.getOutput(),
                        "",
                        javaResult.getError(),
                        javaExecutionTime,
                        0,
                        System.currentTimeMillis() - startTime
                );
            }

            // Step 2: Get Java output
            String javaOutput = javaResult.getOutput();

            // Step 3: Inject Java output into Python code
            String escapedJavaOutput =
                    javaOutput
                            .replace("\\", "\\\\")
                            .replace("\"", "\\\"")
                            .replace("\r", "")
                            .replace("\n", "\\n");

            String pythonCodeWithInput =
                    "java_data = \"" +
                    escapedJavaOutput +
                    "\"\n" +
                    pythonCode;

            // Step 4: Execute Python code
            PythonExecutor pythonExecutor =
                    new PythonExecutor();

            ExecutionResult pythonResult =
                    pythonExecutor.execute(pythonCodeWithInput);

            long pythonExecutionTime =
                    pythonResult.getExecutionTime();

            long totalExecutionTime =
                    System.currentTimeMillis() - startTime;

            if (!pythonResult.isSuccess()) {

                return new DataExchangeResult(
                        false,
                        javaOutput,
                        pythonResult.getOutput(),
                        pythonResult.getError(),
                        javaExecutionTime,
                        pythonExecutionTime,
                        totalExecutionTime
                );
            }

            // Step 5: Return combined result
            return new DataExchangeResult(
                    true,
                    javaOutput,
                    pythonResult.getOutput(),
                    null,
                    javaExecutionTime,
                    pythonExecutionTime,
                    totalExecutionTime
            );

        } catch (Exception e) {

            return new DataExchangeResult(
                    false,
                    "",
                    "",
                    e.getMessage(),
                    0,
                    0,
                    System.currentTimeMillis() - startTime
            );
        }
    }
}