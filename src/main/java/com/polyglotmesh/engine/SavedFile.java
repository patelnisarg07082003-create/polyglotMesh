package com.polyglotmesh.engine;

public class SavedFile {

    private String name;
    private String language;
    private String code;

    public SavedFile() {
    }

    public SavedFile(String name, String language, String code) {
        this.name = name;
        this.language = language;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}