package com.gpa.planner.agent;

import com.gpa.planner.model.Goal;
import com.gpa.planner.model.Task;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AgentPlanner {

    public List<Task> generatePlan(Goal goal) {

        List<Task> tasks = new ArrayList<>();

        int numberOfTasks;

        // 🎯 Basic AI logic (decision making)
        switch (goal.getDifficulty().toUpperCase()) {
            case "HIGH":
                numberOfTasks = 6;
                break;
            case "MEDIUM":
                numberOfTasks = 4;
                break;
            default:
                numberOfTasks = 2;
        }

        for (int i = 1; i <= numberOfTasks; i++) {
            Task task = new Task();

            task.setTitle(goal.getTitle() + " - Step " + i);
            task.setStatus("PENDING");

            // Priority logic
            if (i <= numberOfTasks / 2) {
                task.setPriority("HIGH");
            } else {
                task.setPriority("MEDIUM");
            }

            // Time estimation logic
            task.setEstimatedTime(2 + i);

            task.setGoal(goal);

            tasks.add(task);
        }

        return tasks;
    }
}