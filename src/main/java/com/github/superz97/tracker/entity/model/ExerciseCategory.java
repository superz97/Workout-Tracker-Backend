package com.github.superz97.tracker.entity.model;

public enum ExerciseCategory {

    STRENGTH("Strength"),
    CARDIO("Cardio"),
    FLEXIBILITY("Flexibility"),
    BALANCE("Balance"),
    PLYOMETRIC("Plyometric"),
    POWERLIFTING("Powerlifting"),
    OLYMPIC("Olympic"),
    CALISTHENICS("Calisthenics");

    private final String displayName;

    ExerciseCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
