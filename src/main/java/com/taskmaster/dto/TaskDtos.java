package com.taskmaster.dto;
import com.taskmaster.entity.TaskStatus;
import jakarta.validation.constraints.*;
import java.time.Instant;
public final class TaskDtos {
    private TaskDtos() {}
    public record CreateTaskRequest(@NotBlank @Size(max=200) String title, @Size(max=5000) String description, Instant dueDate, @NotNull Long teamId, Long assigneeId) {}
    public record UpdateTaskRequest(@NotBlank @Size(max=200) String title, @Size(max=5000) String description, @NotNull TaskStatus status, Instant dueDate, Long assigneeId) {}
    public record TaskResponse(Long id, String title, String description, TaskStatus status, Instant dueDate, Long creatorId, Long assigneeId, Long teamId, Instant createdAt, Instant updatedAt) {}
    public record CommentRequest(@NotBlank @Size(max=3000) String content) {}
    public record CommentResponse(Long id, String content, Long authorId, String authorName, Instant createdAt) {}
    public record AttachmentRequest(@NotBlank @Size(max=255) String fileName, @NotBlank @Size(max=1000) String storageUrl, @Positive long fileSize, @Size(max=100) String contentType) {}
    public record AttachmentResponse(Long id, String fileName, String storageUrl, long fileSize, String contentType, Long uploadedBy, Instant createdAt) {}
}
