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

    private String title;
    private String status; // PENDING, DONE
    private String priority; // LOW, MEDIUM, HIGH
    private int estimatedTime;

    @ManyToOne
    private Goal goal;
}