package com.gpa.planner.controller;

import com.gpa.planner.model.Goal;
import com.gpa.planner.model.Task;
import com.gpa.planner.service.AiPlanningService;
import com.gpa.planner.service.GoalService;
import com.gpa.planner.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goals")
public class GoalController {

    private final GoalService goalService;
    private final ScheduleService scheduleService;
    private final AiPlanningService aiPlanningService;

    public GoalController(GoalService goalService,
                          AiPlanningService aiPlanningService,
                          ScheduleService scheduleService) {
        this.goalService = goalService;
        this.aiPlanningService = aiPlanningService;
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public List<Task> createGoalAndPlan(@RequestBody Goal goal) throws Exception {

        // Save goal
        Goal savedGoal = goalService.createGoal(goal);

        // Generate tasks
        List<Task> tasks = aiPlanningService.createPlan(savedGoal);

        // Generate schedule (fix added)
        scheduleService.generateSchedule(tasks);

        return tasks;
    }

    @GetMapping
    public List<Goal> getAllGoals() {
        return goalService.getAllGoals();
    }
}