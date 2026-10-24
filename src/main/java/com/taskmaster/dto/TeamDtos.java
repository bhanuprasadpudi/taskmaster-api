package com.taskmaster.dto;
import jakarta.validation.constraints.*;
public final class TeamDtos {
    private TeamDtos() {}
    public record CreateTeamRequest(@NotBlank @Size(max=120) String name, @Size(max=500) String description) {}
    public record TeamResponse(Long id, String name, String description, Long ownerId, int memberCount) {}
}
