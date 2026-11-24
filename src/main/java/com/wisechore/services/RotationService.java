package com.wisechore.services;

import com.wisechore.models.Chore;
import com.wisechore.models.ChoreStatus;
import com.wisechore.models.CompletionLog;
import com.wisechore.repositories.ChoreRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * SOLID note (Single Responsibility Principle):
 * RotationService does exactly one job: mark a chore done and rotate it
 * to the next roommate. It knows nothing about overdue logic or fairness
 * math -- those are separate classes with separate reasons to change.
 *
 * SOLID note (Dependency Inversion Principle):
 * The constructor takes a ChoreRepository (the interface), not
 * InMemoryChoreRepository (a concrete class). This service doesn't know
 * or care how chores are stored.
 */
public class RotationService {

    private final ChoreRepository repository;

    public RotationService(ChoreRepository repository) {
        this.repository = repository;
    }

    public Chore markDone(String choreId, LocalDate completedOn) {
        Chore chore = repository.get(choreId)
                .orElseThrow(() -> new NoSuchElementException("No chore found with id " + choreId));

        LocalDate effectiveDate = (completedOn != null) ? completedOn : LocalDate.now();

        // Log completion before rotating, so history is preserved.
        repository.addCompletionLog(
                new CompletionLog(chore.getId(), chore.getName(), chore.getCurrentRoommate(), effectiveDate)
        );

        chore.setLastCompletedDate(effectiveDate);
        chore.setCurrentRoommate(nextRoommate(chore));
        chore.setNextDueDate(effectiveDate.plusDays(chore.getFrequency().getDays()));
        chore.setStatus(ChoreStatus.PENDING);

        repository.update(chore);
        return chore;
    }

    public Chore markDone(String choreId) {
        return markDone(choreId, LocalDate.now());
    }

    private String nextRoommate(Chore chore) {
        List<String> rotation = chore.getRoommatesInRotation();
        int currentIndex = rotation.indexOf(chore.getCurrentRoommate());
        int nextIndex = (currentIndex + 1) % rotation.size();
        return rotation.get(nextIndex);
    }
}
