package com.polyglotmesh.engine;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class FileService {

    private final List<SavedFile> files = new ArrayList<>();

    public SavedFile saveFile(SavedFile file) {

        for (int i = 0; i < files.size(); i++) {

            if (files.get(i).getName().equals(file.getName())) {
                files.set(i, file);
                return file;
            }
        }

        files.add(file);
        return file;
    }

    public List<SavedFile> getAllFiles() {
        return files;
    }

    public SavedFile getFile(String name) {

        for (SavedFile file : files) {

            if (file.getName().equals(name)) {
                return file;
            }
        }

        return null;
    }

    public boolean deleteFile(String name) {
        return files.removeIf(file ->
                file.getName().equals(name)
        );
    }
}