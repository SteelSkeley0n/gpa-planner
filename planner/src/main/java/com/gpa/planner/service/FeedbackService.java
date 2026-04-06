package com.gpa.planner.service;

import com.gpa.planner.agent.FeedbackManager;
import com.gpa.planner.model.FeedbackEvent;
import com.gpa.planner.model.Task;
import com.gpa.planner.repository.FeedbackRepository;
import com.gpa.planner.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final TaskRepository taskRepository;
    private final FeedbackManager feedbackManager;

    public FeedbackService(FeedbackRepository feedbackRepository,
                           TaskRepository taskRepository,
                           FeedbackManager feedbackManager) {
        this.feedbackRepository = feedbackRepository;
        this.taskRepository = taskRepository;
        this.feedbackManager = feedbackManager;
    }

    public FeedbackEvent saveFeedback(FeedbackEvent feedback) {

        Task task = feedback.getTask();

        // Existing adaptive logic (keep this)
        feedbackManager.adjustTaskBasedOnFeedback(task, feedback.getResult());

        taskRepository.save(task);

        return feedbackRepository.save(feedback);
    }

    // NEW METHOD (for AI planning adaptation)
    public String getDifficultyAdjustment(Long goalId) {

        List<FeedbackEvent> feedbacks = feedbackRepository.findAll();

        if (feedbacks.isEmpty()) {
            return "MEDIUM";
        }

        long successCount = feedbacks.stream()
                .filter(f -> "SUCCESS".equalsIgnoreCase(f.getResult()))
                .count();

        long failCount = feedbacks.stream()
                .filter(f -> "FAIL".equalsIgnoreCase(f.getResult()))
                .count();

        if (failCount > successCount) {
            return "EASY";
        } else if (successCount > failCount) {
            return "HARD";
        }

        return "MEDIUM";
    }
}