package com.example.tm.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tasks")
public class Task {
    public Task() {}


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "status_name")
    private Status status;

    @Column(name = "priority")
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "creater_id")
    User creater;

    @ManyToOne
    @JoinColumn(name = "executor_id")
    User executor;
}
