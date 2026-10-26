package com.polyglotmesh.engine;

public class DataExchangeResult {

    private boolean success;
    private String javaOutput;
    private String pythonOutput;
    private String error;

    private long javaExecutionTime;
    private long pythonExecutionTime;
    private long totalExecutionTime;

    public DataExchangeResult() {
    }

    public DataExchangeResult(
            boolean success,
            String javaOutput,
            String pythonOutput,
            String error,
            long javaExecutionTime,
            long pythonExecutionTime,
            long totalExecutionTime) {

        this.success = success;
        this.javaOutput = javaOutput;
        this.pythonOutput = pythonOutput;
        this.error = error;
        this.javaExecutionTime = javaExecutionTime;
        this.pythonExecutionTime = pythonExecutionTime;
        this.totalExecutionTime = totalExecutionTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getJavaOutput() {
        return javaOutput;
    }

    public void setJavaOutput(String javaOutput) {
        this.javaOutput = javaOutput;
    }

    public String getPythonOutput() {
        return pythonOutput;
    }

    public void setPythonOutput(String pythonOutput) {
        this.pythonOutput = pythonOutput;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public long getJavaExecutionTime() {
        return javaExecutionTime;
    }

    public void setJavaExecutionTime(long javaExecutionTime) {
        this.javaExecutionTime = javaExecutionTime;
    }

    public long getPythonExecutionTime() {
        return pythonExecutionTime;
    }

    public void setPythonExecutionTime(long pythonExecutionTime) {
        this.pythonExecutionTime = pythonExecutionTime;
    }

    public long getTotalExecutionTime() {
        return totalExecutionTime;
    }

    public void setTotalExecutionTime(long totalExecutionTime) {
        this.totalExecutionTime = totalExecutionTime;
    }
}