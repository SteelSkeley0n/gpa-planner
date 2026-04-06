package com.gpa.planner.repository;

import com.gpa.planner.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<Goal, Long> {
}