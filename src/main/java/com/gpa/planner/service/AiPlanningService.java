package com.gpa.planner.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpa.planner.model.Goal;
import com.gpa.planner.model.Task;
import com.gpa.planner.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AiPlanningService {

    private final OpenRouterService openRouterService;
    private final TaskRepository taskRepository;

    public AiPlanningService(OpenRouterService openRouterService, TaskRepository taskRepository) {
        this.openRouterService = openRouterService;
        this.taskRepository = taskRepository;
    }

    public List<Task> createPlan(Goal goal) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            String response = openRouterService.generatePlan(goal.getTitle(), goal.getDescription());

            JsonNode root = mapper.readTree(response);

            System.out.println("FULL RESPONSE: " + root.toPrettyString());

            JsonNode candidates = root.path("candidates");

            if (!candidates.isArray() || candidates.size() == 0) {
                throw new RuntimeException("No candidates in AI response");
            }

            String content = candidates.get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

            System.out.println("AI CONTENT: " + content);

            content = content.replace("```json", "")
                    .replace("```", "")
                    .trim();

            JsonNode taskRoot = mapper.readTree(content);
            JsonNode tasksNode = taskRoot.path("tasks");

            if (!tasksNode.isArray()) {
                throw new RuntimeException("Tasks not found");
            }

            List<Task> tasks = new ArrayList<>();

            for (JsonNode node : tasksNode) {
                Task task = new Task();

                task.setTitle(node.path("title").asText());
                task.setPriority(node.path("difficulty").asText());
                task.setEstimatedTime(node.path("estimatedHours").asInt());
                task.setStatus("PENDING");
                task.setGoal(goal);

                tasks.add(task);
            }

            return taskRepository.saveAll(tasks);

        } catch (Exception e) {

            System.out.println("AI FAILED, USING FALLBACK: " + e.getMessage());

            // Fallback tasks
            List<Task> fallbackTasks = new ArrayList<>();

            Task t1 = new Task();
            t1.setTitle("Understand basics of " + goal.getTitle());
            t1.setPriority("EASY");
            t1.setEstimatedTime(2);
            t1.setStatus("PENDING");
            t1.setGoal(goal);

            Task t2 = new Task();
            t2.setTitle("Practice and build small project for " + goal.getTitle());
            t2.setPriority("MEDIUM");
            t2.setEstimatedTime(4);
            t2.setStatus("PENDING");
            t2.setGoal(goal);

            fallbackTasks.add(t1);
            fallbackTasks.add(t2);

            return taskRepository.saveAll(fallbackTasks);
        }
    }
}