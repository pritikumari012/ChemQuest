package com.chemquest.service;

import com.chemquest.model.ExperimentCategory;

import java.util.EnumMap;
import java.util.Map;

public class ProgressService {
    private final Map<ExperimentCategory, CategoryProgress> categories =
            new EnumMap<>(ExperimentCategory.class);

    public ProgressService() {
        for (ExperimentCategory category : ExperimentCategory.values()) {
            categories.put(category, new CategoryProgress());
        }
    }

    public synchronized void recordQuiz(ExperimentCategory category, int questions, int correct) {
        if (category == null || questions < 0 || correct < 0 || correct > questions) {
            throw new IllegalArgumentException("Invalid quiz progress values.");
        }
        CategoryProgress progress = categories.get(category);
        progress.attempted += questions;
        progress.correct += correct;
        progress.quizzes++;
    }

    public synchronized CategorySnapshot category(ExperimentCategory category) {
        CategoryProgress progress = categories.get(category);
        return new CategorySnapshot(progress.attempted, progress.correct, progress.quizzes);
    }

    public synchronized int totalAttempted() {
        return categories.values().stream().mapToInt(progress -> progress.attempted).sum();
    }

    public synchronized int totalCorrect() {
        return categories.values().stream().mapToInt(progress -> progress.correct).sum();
    }

    public synchronized int completedQuizzes() {
        return categories.values().stream().mapToInt(progress -> progress.quizzes).sum();
    }

    public String badgeName() {
        int attempted = totalAttempted();
        if (attempted >= 200) {
            return "Excellent ★★★★★";
        }
        if (attempted >= 100) {
            return "Very Good";
        }
        if (attempted >= 50) {
            return "Good";
        }
        return "Keep Practising";
    }

    public String badgeProgress() {
        int attempted = totalAttempted();
        if (attempted >= 200) {
            return "Top badge earned: 200 questions completed.";
        }
        int next = attempted >= 100 ? 200 : attempted >= 50 ? 100 : 50;
        return (next - attempted) + " more answered questions to reach the next badge.";
    }

    public record CategorySnapshot(int attempted, int correct, int quizzes) {
        public int accuracyPercent() {
            return attempted == 0 ? 0 : (int) Math.round(correct * 100.0 / attempted);
        }
    }

    private static final class CategoryProgress {
        private int attempted;
        private int correct;
        private int quizzes;
    }
}
