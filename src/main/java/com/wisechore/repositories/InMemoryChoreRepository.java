package com.wisechore.repositories;

import com.wisechore.models.Chore;
import com.wisechore.models.CompletionLog;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * SOLID note (Liskov Substitution Principle):
 * This class can be substituted anywhere a ChoreRepository is expected
 * (and later, so can a JDBC-backed implementation) without breaking any
 * service that depends on the interface.
 */
public class InMemoryChoreRepository implements ChoreRepository {

    private final Map<String, Chore> chores = new LinkedHashMap<>();
    private final List<CompletionLog> logs = new ArrayList<>();

    @Override
    public void add(Chore chore) {
        chores.put(chore.getId(), chore);
    }

    @Override
    public Optional<Chore> get(String choreId) {
        return Optional.ofNullable(chores.get(choreId));
    }

    @Override
    public List<Chore> getAll() {
        return new ArrayList<>(chores.values());
    }

    @Override
    public void update(Chore chore) {
        chores.put(chore.getId(), chore);
    }

    @Override
    public void delete(String choreId) {
        chores.remove(choreId);
    }

    @Override
    public void addCompletionLog(CompletionLog log) {
        logs.add(log);
    }

    @Override
    public List<CompletionLog> getCompletionLogs(String choreId) {
        List<CompletionLog> result = new ArrayList<>();
        for (CompletionLog log : logs) {
            if (log.getChoreId().equals(choreId)) {
                result.add(log);
            }
        }
        return result;
    }

    @Override
    public List<CompletionLog> getAllCompletionLogs() {
        return new ArrayList<>(logs);
    }
}
