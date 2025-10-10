package com.github.superz97.tracker.entity.model;

public enum WorkoutStatus {

    SCHEDULED("Scheduled"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    MISSED("Missed");

    private final String displayName;

    WorkoutStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
