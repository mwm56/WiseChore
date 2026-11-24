package com.wisechore.services;

import com.wisechore.models.Chore;
import com.wisechore.models.Frequency;
import com.wisechore.repositories.ChoreRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SOLID note (Single Responsibility): this class owns the rules for
 * creating a valid chore and listing chores. It does not rotate, check
 * overdue, or print anything.
 */
public class ChoreService {

    private final ChoreRepository repository;

    public ChoreService(ChoreRepository repository) {
        this.repository = repository;
    }

    /** Validates the input and saves a new chore. Throws IllegalArgumentException with a user-friendly message if invalid. */
    public Chore addChore(String name, Frequency frequency, List<String> roommates, LocalDate firstDueDate) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Chore name cannot be empty.");
        }
        if (name.contains("|")) {
            throw new IllegalArgumentException("Chore name cannot contain the '|' character.");
        }
        if (frequency == null) {
            throw new IllegalArgumentException("Please choose a frequency.");
        }
        if (firstDueDate == null) {
            throw new IllegalArgumentException("Please enter a due date.");
        }

        List<String> cleaned = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (String r : roommates) {
            String trimmed = r.trim();
            if (trimmed.isEmpty()) continue;
            if (trimmed.contains("|")) {
                throw new IllegalArgumentException("Roommate names cannot contain the '|' character.");
            }
            if (!seen.add(trimmed.toLowerCase())) {
                throw new IllegalArgumentException("Roommate '" + trimmed + "' is listed twice.");
            }
            cleaned.add(trimmed);
        }
        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("Enter at least one roommate.");
        }

        Chore chore = new Chore(name.trim(), frequency, cleaned, cleaned.get(0), firstDueDate);
        repository.add(chore);
        return chore;
    }

    /** All chores, soonest due date first. */
    public List<Chore> listChoresByDueDate() {
        List<Chore> chores = repository.getAll();
        chores.sort(Comparator.comparing(Chore::getNextDueDate));
        return chores;
    }
}
