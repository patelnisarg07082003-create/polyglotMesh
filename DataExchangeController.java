package com.polyglotmesh.engine;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/data-exchange")
@CrossOrigin(origins = "http://localhost:5173")
public class DataExchangeController {

    private final DataExchangeService dataExchangeService;

    public DataExchangeController(
            DataExchangeService dataExchangeService) {

        this.dataExchangeService = dataExchangeService;
    }

    @PostMapping("/java-to-python")
    public DataExchangeResult executeJavaToPython(
            @RequestBody DataExchangeRequest request) {

        return dataExchangeService.executeJavaToPython(
                request.getJavaCode(),
                request.getPythonCode()
        );
    }
}