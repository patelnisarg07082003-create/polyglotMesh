package com.polyglotmesh.engine;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:5173")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public SavedFile saveFile(@RequestBody SavedFile file) {
        return fileService.saveFile(file);
    }

    @GetMapping
    public List<SavedFile> getAllFiles() {
        return fileService.getAllFiles();
    }

    @GetMapping("/{name}")
    public SavedFile getFile(@PathVariable String name) {
        return fileService.getFile(name);
    }

    @DeleteMapping("/{name}")
    public void deleteFile(@PathVariable String name) {
        fileService.deleteFile(name);
    }
}