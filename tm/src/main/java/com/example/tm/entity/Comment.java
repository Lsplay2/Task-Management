package com.example.tm.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "comments")
@Entity
public class Comment {
    public Comment() {}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "creater_id")
    User creater;

    @ManyToOne
    @JoinColumn(name = "task_id")
    Task task;

}
