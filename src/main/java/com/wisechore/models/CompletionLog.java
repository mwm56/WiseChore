package com.wisechore.models;

import java.time.LocalDate;
import java.util.UUID;

public class CompletionLog {

    private final String id;
    private final String choreId;
    private final String choreName;
    private final String roommate;
    private final LocalDate completedOn;

    public CompletionLog(String choreId, String choreName, String roommate, LocalDate completedOn) {
        this.id = UUID.randomUUID().toString();
        this.choreId = choreId;
        this.choreName = choreName;
        this.roommate = roommate;
        this.completedOn = completedOn;
    }

    public String getId() {
        return id;
    }

    public String getChoreId() {
        return choreId;
    }

    public String getChoreName() {
        return choreName;
    }

    public String getRoommate() {
        return roommate;
    }

    public LocalDate getCompletedOn() {
        return completedOn;
    }
}
