package com.polyglotmesh.model;

import java.time.LocalDateTime;

public class ExecutionHistory {

    private String code;
    private String output;
    private String error;
    private boolean success;
    private long executionTime;
    private LocalDateTime executedAt;

    public ExecutionHistory(
            String code,
            String output,
            String error,
            boolean success,
            long executionTime
    ) {

        this.code = code;
        this.output = output;
        this.error = error;
        this.success = success;
        this.executionTime = executionTime;
        this.executedAt = LocalDateTime.now();
    }

    public String getCode() {
        return code;
    }

    public String getOutput() {
        return output;
    }

    public String getError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }
}