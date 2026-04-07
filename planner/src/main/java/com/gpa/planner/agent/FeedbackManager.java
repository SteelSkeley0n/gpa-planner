package com.gpa.planner.agent;

import com.gpa.planner.model.Task;
import org.springframework.stereotype.Component;

@Component
public class FeedbackManager {

    public void adjustTaskBasedOnScore(Task task, int score, String currentResult) {

        // ----------------------------
        // Difficulty Adjustment (based on history)
        // ----------------------------
        if (score >= 3) {
            if ("EASY".equalsIgnoreCase(task.getPriority())) {
                task.setPriority("MEDIUM");
            } else if ("MEDIUM".equalsIgnoreCase(task.getPriority())) {
                task.setPriority("HARD");
            }
        } else if (score <= -2) {
            if ("HARD".equalsIgnoreCase(task.getPriority())) {
                task.setPriority("MEDIUM");
            } else if ("MEDIUM".equalsIgnoreCase(task.getPriority())) {
                task.setPriority("EASY");
            }
        }

        // ----------------------------
        // Time Adjustment (based on current action)
        // ----------------------------
        if ("SUCCESS".equalsIgnoreCase(currentResult)) {
            int newTime = task.getEstimatedTime() - 1;
            task.setEstimatedTime(Math.max(newTime, 1));
        } else {
            task.setEstimatedTime(task.getEstimatedTime() + 1);
        }

        // ----------------------------
        // Status Update (based on current action)
        // ----------------------------
        if ("SUCCESS".equalsIgnoreCase(currentResult)) {
            task.setStatus("COMPLETED");
        } else {
            if (!"COMPLETED".equalsIgnoreCase(task.getStatus())) {
                task.setStatus("PENDING");
            }
        }
    }
}