package com.gpa.planner.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDate deadline;
    private String difficulty;

    @Column(name = "remaining_time")
    private Integer remainingTime;

    @ManyToOne
    private User user;
}