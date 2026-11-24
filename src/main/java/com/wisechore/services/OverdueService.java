package com.wisechore.services;

import com.wisechore.models.Chore;
import com.wisechore.models.ChoreStatus;
import com.wisechore.repositories.ChoreRepository;

import java.time.LocalDate;

/**
 * SOLID note (Single Responsibility Principle):
 * OverdueService's only job is deciding whether chores are overdue. If
 * the "what counts as overdue" rule changes, this is the only file that
 * should need editing.
 */
public class OverdueService {

    private final ChoreRepository repository;

    public OverdueService(ChoreRepository repository) {
        this.repository = repository;
    }

    /**
     * Scans all chores and flips any Pending chore whose due date has
     * passed into Overdue. Returns how many chores were updated.
     */
    public int refreshOverdueStatuses(LocalDate today) {
        LocalDate effectiveToday = (today != null) ? today : LocalDate.now();
        int updatedCount = 0;

        for (Chore chore : repository.getAll()) {
            if (chore.getStatus() == ChoreStatus.PENDING
                    && chore.getNextDueDate().isBefore(effectiveToday)) {
                chore.setStatus(ChoreStatus.OVERDUE);
                repository.update(chore);
                updatedCount++;
            }
        }

        return updatedCount;
    }

    public int refreshOverdueStatuses() {
        return refreshOverdueStatuses(LocalDate.now());
    }
}
