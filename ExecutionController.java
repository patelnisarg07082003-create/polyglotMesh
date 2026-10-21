package com.polyglotmesh.engine;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class ExecutionController {

    private final ExecutionService executionService;

    public ExecutionController(ExecutionService executionService) {
        this.executionService = executionService;
    }

    @PostMapping("/execute")
    public ExecutionResult execute(
            @RequestBody ExecutionRequest request) {

        return executionService.execute(
                request.getLanguage(),
                request.getCode()
        );
    }

    @PostMapping("/interop/python")
    public Object pythonInterop(
            @RequestBody Map<String, Object> request) {

        String code =
                (String) request.get("code");

        String variable =
                (String) request.get("variable");

        Object value =
                request.get("value");

        PythonExecutor pythonExecutor =
                new PythonExecutor();

        return pythonExecutor.executeInteropWithMetrics(
                code,
                variable,
                value
        );
    }
}