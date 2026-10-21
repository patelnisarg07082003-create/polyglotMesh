package com.polyglotmesh.engine;

public class ExecutionTest {

    public static void main(String[] args) {

        ExecutionService service = new ExecutionService();

        System.out.println("===== PYTHON =====");

        ExecutionResult pythonResult =
                service.execute(
                        "python",
                        "print('Hello from Python')\nprint(10 + 20)"
                );

        System.out.println("Success: " + pythonResult.isSuccess());
        System.out.println("Output:\n" + pythonResult.getOutput());
        System.out.println("Error: " + pythonResult.getError());


        System.out.println("\n===== JAVASCRIPT =====");

        ExecutionResult jsResult =
                service.execute(
                        "javascript",
                        "print('Hello from JavaScript');\nprint(100 + 200);"
                );

        System.out.println("Success: " + jsResult.isSuccess());
        System.out.println("Output:\n" + jsResult.getOutput());
        System.out.println("Error: " + jsResult.getError());
    }
}