package com.wisechore.models;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * SOLID note (Single Responsibility Principle):
 * Chore only holds data about a chore. It has no idea how rotation works,
 * what counts as overdue, or how fairness is calculated -- that logic
 * lives in the service classes. If a rule changes, this class should not
 * need to change.
 */
public class Chore {

    private final String id;
    private String name;
    private Frequency frequency;
    private List<String> roommatesInRotation;
    private String currentRoommate;
    private LocalDate nextDueDate;
    private ChoreStatus status;
    private LocalDate lastCompletedDate;

    public Chore(String name,
                 Frequency frequency,
                 List<String> roommatesInRotation,
                 String currentRoommate,
                 LocalDate nextDueDate) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.frequency = frequency;
        this.roommatesInRotation = roommatesInRotation;
        this.currentRoommate = currentRoommate;
        this.nextDueDate = nextDueDate;
        this.status = ChoreStatus.PENDING;
        this.lastCompletedDate = null;
    }

    /** Used when loading a saved chore from storage (keeps its original id and state). */
    public Chore(String id,
                 String name,
                 Frequency frequency,
                 List<String> roommatesInRotation,
                 String currentRoommate,
                 LocalDate nextDueDate,
                 ChoreStatus status,
                 LocalDate lastCompletedDate) {
        this.id = id;
        this.name = name;
        this.frequency = frequency;
        this.roommatesInRotation = roommatesInRotation;
        this.currentRoommate = currentRoommate;
        this.nextDueDate = nextDueDate;
        this.status = status;
        this.lastCompletedDate = lastCompletedDate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public List<String> getRoommatesInRotation() {
        return roommatesInRotation;
    }

    public String getCurrentRoommate() {
        return currentRoommate;
    }

    public void setCurrentRoommate(String currentRoommate) {
        this.currentRoommate = currentRoommate;
    }

    public LocalDate getNextDueDate() {
        return nextDueDate;
    }

    public void setNextDueDate(LocalDate nextDueDate) {
        this.nextDueDate = nextDueDate;
    }

    public ChoreStatus getStatus() {
        return status;
    }

    public void setStatus(ChoreStatus status) {
        this.status = status;
    }

    public LocalDate getLastCompletedDate() {
        return lastCompletedDate;
    }

    public void setLastCompletedDate(LocalDate lastCompletedDate) {
        this.lastCompletedDate = lastCompletedDate;
    }

    @Override
    public String toString() {
        return "Chore{" +
                "name='" + name + '\'' +
                ", frequency=" + frequency +
                ", currentRoommate='" + currentRoommate + '\'' +
                ", status=" + status +
                ", nextDueDate=" + nextDueDate +
                '}';
    }
}
