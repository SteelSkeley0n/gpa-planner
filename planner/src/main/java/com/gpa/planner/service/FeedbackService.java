package com.gpa.planner.service;

import com.gpa.planner.agent.FeedbackManager;
import com.gpa.planner.model.FeedbackEvent;
import com.gpa.planner.model.Task;
import com.gpa.planner.repository.FeedbackRepository;
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

    public FeedbackService(FeedbackRepository feedbackRepository,
                           TaskRepository taskRepository,
                           FeedbackManager feedbackManager) {
        this.feedbackRepository = feedbackRepository;
        this.taskRepository = taskRepository;
        this.feedbackManager = feedbackManager;
    }

    @Transactional
    public FeedbackEvent processFeedback(Long taskId, String result) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Get past feedback
        List<FeedbackEvent> feedbacks = feedbackRepository.findByTaskId(taskId);

        int score = 0;

        for (FeedbackEvent f : feedbacks) {
            if ("SUCCESS".equalsIgnoreCase(f.getResult())) {
                score++;
            } else {
                score--;
            }
        }

        // Include current feedback
        if ("SUCCESS".equalsIgnoreCase(result)) {
            score++;
        } else {
            score--;
        }

        // Apply task adaptation
        int originalTime = task.getEstimatedTime();

        feedbackManager.adjustTaskBasedOnScore(task, score, result);

        // ----------------------------
        // 🔥 Goal Time Update
        // ----------------------------
        if ("SUCCESS".equalsIgnoreCase(result)) {
            if (task.getGoal() != null && task.getGoal().getRemainingTime() != null) {

                int updatedTime = task.getGoal().getRemainingTime() - originalTime;

                task.getGoal().setRemainingTime(Math.max(updatedTime, 0));
            }
        }

        task = taskRepository.saveAndFlush(task);

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
}