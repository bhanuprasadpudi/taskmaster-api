package com.taskmaster.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.util.*;

@Entity
@Table(name = "tasks", indexes = {
        @Index(name = "idx_task_status", columnList = "status"), @Index(name = "idx_task_assignee", columnList = "assignee_id"),
        @Index(name = "idx_task_team", columnList = "team_id"), @Index(name = "idx_task_due_date", columnList = "due_date")})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Task {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 200) private String title;
    @Column(length = 5000) private String description;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) @Builder.Default private TaskStatus status = TaskStatus.OPEN;
    private Instant dueDate;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "creator_id") private User creator;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "assignee_id") private User assignee;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "team_id") private Team team;
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true) @Builder.Default private List<Comment> comments = new ArrayList<>();
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true) @Builder.Default private List<Attachment> attachments = new ArrayList<>();
    @Column(nullable = false, updatable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;
    @PrePersist void prePersist() { createdAt = Instant.now(); updatedAt = createdAt; }
    @PreUpdate void preUpdate() { updatedAt = Instant.now(); }
}
