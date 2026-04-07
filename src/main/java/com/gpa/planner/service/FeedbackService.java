package com.gpa.planner.service;

import com.gpa.planner.agent.FeedbackManager;
import com.gpa.planner.model.FeedbackEvent;
import com.gpa.planner.model.Task;
import com.gpa.planner.repository.FeedbackRepository;
import com.gpa.planner.repository.GoalRepository;
import com.gpa.planner.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final TaskRepository taskRepository;
    private final FeedbackManager feedbackManager;
    private final GoalRepository goalRepository;

    public FeedbackService(FeedbackRepository feedbackRepository,
                           TaskRepository taskRepository,
                           FeedbackManager feedbackManager, GoalRepository goalRepository) {
        this.feedbackRepository = feedbackRepository;
        this.taskRepository = taskRepository;
        this.feedbackManager = feedbackManager;
        this.goalRepository = goalRepository;
    }

    @Transactional
    public FeedbackEvent processFeedback(Long taskId, String result) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        int originalTime = task.getEstimatedTime();

        // ----------------------------
        // 🔹 NEW: Use streak-based logic
        // ----------------------------
        feedbackManager.adjustTaskBasedOnFeedback(task, result);

        // ----------------------------
        // 🔥 Goal Time Update (FIXED)
        // ----------------------------
        if ("SUCCESS".equalsIgnoreCase(result)) {
            if (task.getGoal() != null && task.getGoal().getRemainingTime() != null) {

                int updatedTime = task.getGoal().getRemainingTime() - originalTime;

                task.getGoal().setRemainingTime(Math.max(updatedTime, 0));

                // 🔥 THIS IS THE REAL FIX
                goalRepository.save(task.getGoal());
            }
        }

        task = taskRepository.saveAndFlush(task);

        // ----------------------------
        // 🔹 Save Feedback Event
        // ----------------------------
        FeedbackEvent feedback = new FeedbackEvent();
        feedback.setTask(task);
        feedback.setResult(result);
        feedback.setTimestamp(LocalDateTime.now());

        return feedbackRepository.save(feedback);
    }

    // Existing method (unchanged)
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

    public double getOverallRecentScore() {

        List<FeedbackEvent> feedbacks = feedbackRepository.findAll();

        if (feedbacks.isEmpty()) return 0.5;

        long success = feedbacks.stream()
                .filter(f -> "SUCCESS".equalsIgnoreCase(f.getResult()))
                .count();

        return (double) success / feedbacks.size();
    }
}