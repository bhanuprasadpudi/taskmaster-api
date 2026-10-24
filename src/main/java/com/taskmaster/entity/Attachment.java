package com.taskmaster.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name = "attachments", indexes = @Index(name = "idx_attachment_task", columnList = "task_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Attachment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 255) private String fileName;
    @Column(nullable = false, length = 1000) private String storageUrl;
    @Column(nullable = false) private long fileSize;
    @Column(length = 100) private String contentType;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "task_id") private Task task;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "uploaded_by") private User uploadedBy;
    @Column(nullable = false, updatable = false) private Instant createdAt;
    @PrePersist void prePersist() { createdAt = Instant.now(); }
}
