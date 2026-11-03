package com.chemquest.model;

import java.util.List;

public record QuizQuestion(String prompt, List<String> options, int correctIndex, String explanation) {
    public QuizQuestion {
        options = List.copyOf(options);
        if (options.size() < 2 || correctIndex < 0 || correctIndex >= options.size()) {
            throw new IllegalArgumentException("A quiz question needs at least two options and a valid answer.");
        }
    }

    public String correctAnswer() {
        return options.get(correctIndex);
    }
}
