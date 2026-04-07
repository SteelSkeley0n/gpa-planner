package com.gpa.planner.service;

import com.gpa.planner.model.Goal;
import com.gpa.planner.model.Task;
import com.gpa.planner.repository.GoalRepository;
import com.gpa.planner.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final GoalRepository goalRepository;

    public TaskService(TaskRepository taskRepository,
                       GoalRepository goalRepository) {
        this.taskRepository = taskRepository;
        this.goalRepository = goalRepository;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task updateStatus(Long taskId, String status) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        task.setStatus(status);
        return taskRepository.save(task);
    }

    // 🔥 NEW METHOD (IMPORTANT)
    public List<Task> saveGeneratedTasks(Goal goal, List<Task> tasks) {

        // Save goal first (with updated remainingTime)
        goalRepository.save(goal);

        // Save all tasks
        return taskRepository.saveAll(tasks);
    }
}