package com.gpa.planner.service;

import com.gpa.planner.model.Task;
import com.gpa.planner.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
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
}