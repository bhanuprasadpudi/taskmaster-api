package com.taskmaster.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "users", indexes = @Index(name = "idx_user_email", columnList = "email", unique = true))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 180)
    private String email;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false)
    private String passwordHash;
    @Column(nullable = false)
    private boolean enabled = true;
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    @PrePersist void prePersist() { createdAt = Instant.now(); }
    @OneToMany(mappedBy = "creator") @Builder.Default private Set<Task> createdTasks = new HashSet<>();
    @OneToMany(mappedBy = "assignee") @Builder.Default private Set<Task> assignedTasks = new HashSet<>();
    @ManyToMany(mappedBy = "members") @Builder.Default private Set<Team> teams = new HashSet<>();
}
