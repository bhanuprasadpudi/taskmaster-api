package com.taskmaster.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "teams", indexes = @Index(name = "idx_team_name", columnList = "name"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Team {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 120) private String name;
    @Column(length = 500) private String description;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "owner_id") private User owner;
    @ManyToMany
    @JoinTable(name = "team_members", joinColumns = @JoinColumn(name = "team_id"), inverseJoinColumns = @JoinColumn(name = "user_id"),
            indexes = {@Index(name = "idx_team_member_team", columnList = "team_id"), @Index(name = "idx_team_member_user", columnList = "user_id")})
    @Builder.Default private Set<User> members = new HashSet<>();
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true) @Builder.Default private Set<Task> tasks = new HashSet<>();
    @Column(nullable = false, updatable = false) private Instant createdAt;
    @PrePersist void prePersist() { createdAt = Instant.now(); }
}
