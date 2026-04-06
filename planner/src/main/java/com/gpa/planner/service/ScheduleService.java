package com.gpa.planner.service;

import com.gpa.planner.model.Schedule;
import com.gpa.planner.model.Task;
import com.gpa.planner.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    // FIXED METHOD
    public void generateSchedule(List<Task> tasks) {

        LocalDateTime currentTime = LocalDateTime.now();

        for (Task task : tasks) {

            Schedule schedule = new Schedule();
            schedule.setTask(task);

            // Start time
            schedule.setStartTime(currentTime);

            // End time based on estimated hours
            schedule.setEndTime(currentTime.plusHours(task.getEstimatedTime()));

            scheduleRepository.save(schedule);

            // Move time forward for next task
            currentTime = schedule.getEndTime().plusHours(1); // 1 hour gap
        }
    }
}