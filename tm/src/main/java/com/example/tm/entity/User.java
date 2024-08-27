package com.example.tm.entity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "names")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
}
