package com.gpa.planner.agent;

import com.gpa.planner.model.FeedbackEvent;
import com.gpa.planner.model.Task;
import com.gpa.planner.repository.FeedbackRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FeedbackManager {

    private final FeedbackRepository feedbackRepository;

    public FeedbackManager(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public void adjustTaskBasedOnFeedback(Task task, String currentResult) {

        double recentScore = calculateRecentScore(task);

        // --- Difficulty Adjustment ---
        if (recentScore >= 0.8) {
            increaseDifficulty(task);
        } else if (recentScore <= 0.3) {
            decreaseDifficulty(task);
        } else {
            keepBalanced(task);
        }

        // --- Time Adjustment + Status ---
        if (currentResult.equalsIgnoreCase("SUCCESS")) {
            task.setStatus("COMPLETED");
            task.setEstimatedTime(Math.max(10, task.getEstimatedTime() - 10));
        } else {
            task.setStatus("PENDING");
            task.setEstimatedTime(task.getEstimatedTime() + 10);
        }
    }

    // ===============================
    // 🔹 NEW: Recent Feedback Score
    // ===============================
    private double calculateRecentScore(Task task) {

        List<FeedbackEvent> recentFeedbacks =
                feedbackRepository.findTop5ByTaskOrderByTimestampDesc(task);

        if (recentFeedbacks.isEmpty()) {
            return 0.5; // neutral behavior
        }

        long successCount = recentFeedbacks.stream()
                .filter(f -> f.getResult().equalsIgnoreCase("SUCCESS"))
                .count();

        return (double) successCount / recentFeedbacks.size();
    }

    // ===============================
    // 🔹 Difficulty Controls
    // ===============================
    private void increaseDifficulty(Task task) {
        task.setEstimatedTime(task.getEstimatedTime() + 10);
        task.setPriority("HIGH");
    }

    private void decreaseDifficulty(Task task) {
        task.setEstimatedTime(Math.max(10, task.getEstimatedTime() - 10));
        task.setPriority("LOW");
    }

    private void keepBalanced(Task task) {
        // No major change
    }
}