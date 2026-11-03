package com.chemquest.model;

public enum ExperimentCategory {
    BASIC("Basic", "Level 1"),
    INTERMEDIATE("Intermediate", "Level 2"),
    ADVANCED("Advanced", "Level 3");

    private final String label;
    private final String level;

    ExperimentCategory(String label, String level) {
        this.label = label;
        this.level = level;
    }

    public String label() {
        return label;
    }

    public String level() {
        return level;
    }
}
