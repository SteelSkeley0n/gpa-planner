package com.gpa.planner.agent;

import com.gpa.planner.model.Task;
import org.springframework.stereotype.Component;

@Component
public class FeedbackManager {

    public void adjustTaskBasedOnFeedback(Task task, String feedback) {

        if (feedback.equalsIgnoreCase("FAIL")) {
            // Reduce difficulty
            task.setEstimatedTime(task.getEstimatedTime() - 1);
            task.setPriority("LOW");
        }

        if (feedback.equalsIgnoreCase("SUCCESS")) {
            // Increase challenge
            task.setEstimatedTime(task.getEstimatedTime() + 1);
            task.setPriority("HIGH");
        }
    }
}