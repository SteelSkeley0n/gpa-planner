package com.gpa.planner.repository;

import com.gpa.planner.model.FeedbackEvent;
import com.gpa.planner.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<FeedbackEvent, Long> {

    // Get last 5 feedbacks for a task (most recent first)
    List<FeedbackEvent> findTop5ByTaskOrderByTimestampDesc(Task task);
}