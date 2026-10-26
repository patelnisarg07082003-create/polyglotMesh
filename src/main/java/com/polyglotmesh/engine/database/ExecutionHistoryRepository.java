package com.polyglotmesh.engine.database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.polyglotmesh.model.ExecutionHistory;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class ExecutionHistoryRepository {

    private final MongoCollection<Document> collection;

    public ExecutionHistoryRepository(MongoDatabase database) {
        this.collection =
                database.getCollection("execution_history");
    }

    // Save execution history
    public void saveExecution(ExecutionHistory history) {

        Document document =
                new Document()
                        .append("code", history.getCode())
                        .append("output", history.getOutput())
                        .append("error", history.getError())
                        .append("success", history.isSuccess())
                        .append(
                                "executionTime",
                                history.getExecutionTime()
                        )
                        .append(
                                "executedAt",
                                history.getExecutedAt().toString()
                        );

        collection.insertOne(document);

        System.out.println(
                "Execution history saved to MongoDB"
        );
    }

    // Get all execution history
    public List<Document> getAllExecutions() {

        List<Document> executions =
                new ArrayList<>();

        FindIterable<Document> documents =
                collection.find();

        for (Document document : documents) {
            executions.add(document);
        }

        return executions;
    }

    // Get recent execution history
    public List<Document> getRecentExecutions(int limit) {

        List<Document> executions =
                new ArrayList<>();

        FindIterable<Document> documents =
                collection.find()
                        .sort(
                                new Document(
                                        "executedAt",
                                        -1
                                )
                        )
                        .limit(limit);

        for (Document document : documents) {
            executions.add(document);
        }

        return executions;
    }

    // Get total executions
    public long getTotalExecutions() {

        return collection.countDocuments();
    }

    // Get successful executions count
    public long getSuccessfulExecutions() {

        return collection.countDocuments(
                new Document("success", true)
        );
    }

    // Get failed executions count
    public long getFailedExecutions() {

        return collection.countDocuments(
                new Document("success", false)
        );
    }

    // Get successful execution history
    public List<Document> getSuccessfulExecutionHistory() {

        List<Document> executions =
                new ArrayList<>();

        FindIterable<Document> documents =
                collection.find(
                        new Document("success", true)
                );

        for (Document document : documents) {
            executions.add(document);
        }

        return executions;
    }

    // Get failed execution history
    public List<Document> getFailedExecutionHistory() {

        List<Document> executions =
                new ArrayList<>();

        FindIterable<Document> documents =
                collection.find(
                        new Document("success", false)
                );

        for (Document document : documents) {
            executions.add(document);
        }

        return executions;
    }

    // Get execution history by status with limit
    public List<Document> getExecutionHistoryByStatus(
            boolean success,
            int limit
    ) {

        List<Document> executions =
                new ArrayList<>();

        FindIterable<Document> documents =
                collection.find(
                        new Document("success", success)
                )
                .sort(
                        new Document(
                                "executedAt",
                                -1
                        )
                )
                .limit(limit);

        for (Document document : documents) {
            executions.add(document);
        }

        return executions;
    }

    // Delete all execution history
    public void deleteAllExecutions() {

        collection.deleteMany(new Document());

        System.out.println(
                "All execution history deleted"
        );
    }

    // Delete one execution by MongoDB ID
    public void deleteExecutionById(String id) {

        collection.deleteOne(
                new Document(
                        "_id",
                        new ObjectId(id)
                )
        );

        System.out.println(
                "Execution deleted successfully"
        );
    }
}