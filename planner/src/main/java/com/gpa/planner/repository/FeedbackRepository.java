package com.gpa.planner.repository;

import com.gpa.planner.model.FeedbackEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<FeedbackEvent, Long> {

    List<FeedbackEvent> findByTaskId(Long taskId);
}