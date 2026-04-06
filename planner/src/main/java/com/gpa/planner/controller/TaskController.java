package com.gpa.planner.controller;

import com.gpa.planner.model.Task;
import com.gpa.planner.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PutMapping("/{id}")
    public Task updateStatus(@PathVariable Long id, @RequestParam String status) {
        return taskService.updateStatus(id, status);
    }
}