package com.gpa.planner.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "estimated_time")
    private int estimatedTime;

    @Column(name = "priority")
    private String priority;

    private String title;
    private String status;

    @ManyToOne
    private Goal goal;
}