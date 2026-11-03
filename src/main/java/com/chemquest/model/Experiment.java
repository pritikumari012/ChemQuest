package com.chemquest.model;

import java.util.List;

public record Experiment(
        String id,
        String title,
        String shortDesc,
        String difficulty,
        int minutes,
        ExperimentCategory category,
        List<String> apparatus,
        String aim,
        String theory,
        List<String> equationLines,
        List<String> reagents,
        List<String> steps,
        List<String> observations,
        String result,
        List<String> calculations,
        List<String> precautions,
        String hazard,
        String lab,
        String summary
) {
    public boolean hasLab() {
        return lab != null && !lab.isBlank();
    }
}
