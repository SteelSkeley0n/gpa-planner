package com.gpa.planner.controller;

import com.gpa.planner.model.FeedbackEvent;
import com.gpa.planner.model.Task;
import com.gpa.planner.repository.TaskRepository;
import com.gpa.planner.service.FeedbackService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final TaskRepository taskRepository;

    public FeedbackController(FeedbackService feedbackService,
                              TaskRepository taskRepository) {
        this.feedbackService = feedbackService;
        this.taskRepository = taskRepository;
    }

    @PostMapping
    public FeedbackEvent submitFeedback(@RequestParam Long taskId,
                                        @RequestParam String result) {

        return feedbackService.processFeedback(taskId, result);
    }
}