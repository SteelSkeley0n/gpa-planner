package com.gpa.planner.controller;

import com.gpa.planner.model.FeedbackEvent;
import com.gpa.planner.service.FeedbackService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public FeedbackEvent giveFeedback(@RequestBody FeedbackEvent feedback) {
        return feedbackService.saveFeedback(feedback);
    }
}