package com.taskmaster.dto;
import jakarta.validation.constraints.NotBlank;
public final class GenAiDtos {
    private GenAiDtos() {}
    public record GenerateRequest(@NotBlank String prompt) {}
    public record GenerateResponse(String description) {}
}
