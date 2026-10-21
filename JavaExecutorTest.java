package com.polyglotmesh.engine;

public class JavaExecutorTest {

    public static void main(String[] args) {

        ExecutionService service = new ExecutionService();

        String code = """
                public class Main {

                    public static void main(String[] args) {

                        System.out.println("Hello from Java");

                        int a = 10;
                        int b = 20;

                        System.out.println("Sum = " + (a + b));
                    }
                }
                """;

        ExecutionResult result =
                service.execute("java", code);

        System.out.println("Success: " + result.isSuccess());
        System.out.println("Output: " + result.getOutput());
        System.out.println("Error: " + result.getError());
        System.out.println(
                "Execution Time: "
                        + result.getExecutionTime()
                        + " ms"
        );
    }
}