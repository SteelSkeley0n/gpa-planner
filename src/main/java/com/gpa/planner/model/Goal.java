package com.gpa.planner.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

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

    // 🔥 ADD THIS
    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonManagedReference
    private List<Task> tasks;
}