package com.polyglotmesh.engine;

public class DataExchangeRequest {

    private String javaCode;
    private String pythonCode;

    public DataExchangeRequest() {
    }

    public String getJavaCode() {
        return javaCode;
    }

    public void setJavaCode(String javaCode) {
        this.javaCode = javaCode;
    }

    public String getPythonCode() {
        return pythonCode;
    }

    public void setPythonCode(String pythonCode) {
        this.pythonCode = pythonCode;
    }
}