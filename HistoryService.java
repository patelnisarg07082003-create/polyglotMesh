package com.polyglotmesh.engine;

import com.mongodb.client.MongoDatabase;
import com.polyglotmesh.engine.database.ExecutionHistoryRepository;
import com.polyglotmesh.engine.database.MongoDBConnection;
import org.bson.Document;

import java.util.List;

public class HistoryService {

    private final ExecutionHistoryRepository repository;

    public HistoryService() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        repository = new ExecutionHistoryRepository(database);
    }

    public List<Document> getRecentHistory() {
        return repository.getRecentExecutions(10);
    }

    public List<Document> getSuccessfulHistory() {
        return repository.getSuccessfulExecutionHistory();
    }

    public List<Document> getFailedHistory() {
        return repository.getFailedExecutionHistory();
    }

    public long getTotalExecutions() {
        return repository.getTotalExecutions();
    }

    public long getSuccessfulExecutions() {
        return repository.getSuccessfulExecutions();
    }

    public long getFailedExecutions() {
        return repository.getFailedExecutions();
    }
}