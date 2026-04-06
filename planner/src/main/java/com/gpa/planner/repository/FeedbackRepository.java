package com.gpa.planner.repository;

import com.gpa.planner.model.FeedbackEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<FeedbackEvent, Long> {
}