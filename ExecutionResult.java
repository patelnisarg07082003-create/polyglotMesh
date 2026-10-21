package com.polyglotmesh.engine;

public class ExecutionResult {

private final boolean success;
private final String output;
private final String error;
private final long executionTime;

    public ExecutionResult(boolean success, String output, String error, long executionTime) {
        this.success = success;
        this.output = output;
        this.error = error;
        this.executionTime = executionTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getOutput() {
        return output;
    }

    public String getError() {
        return error;
    }

    public long getExecutionTime() {
        return executionTime;
    }
}