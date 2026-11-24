package com.wisechore.repositories;

import com.wisechore.models.Chore;
import com.wisechore.models.CompletionLog;

import java.util.List;
import java.util.Optional;

/**
 * SOLID note (Interface Segregation Principle):
 * This interface only exposes what the services actually need -- basic
 * CRUD on chores, plus reading/writing completion logs. It is not a
 * bloated "repository that does everything" interface.
 *
 * SOLID note (Open/Closed + Dependency Inversion):
 * Every service in this app depends on THIS interface, never on a
 * concrete class. Adding persistence later (e.g. a JDBC-backed
 * implementation) means writing a new class that implements
 * ChoreRepository -- existing, tested service code never changes.
 */
public interface ChoreRepository {

    void add(Chore chore);

    Optional<Chore> get(String choreId);

    List<Chore> getAll();

    void update(Chore chore);

    void delete(String choreId);

    void addCompletionLog(CompletionLog log);

    List<CompletionLog> getCompletionLogs(String choreId);

    List<CompletionLog> getAllCompletionLogs();
}
