package com.taskmaster.dto;
import jakarta.validation.constraints.*;
public final class AuthDtos {
    private AuthDtos() {}
    public record RegisterRequest(@NotBlank @Email String email, @NotBlank @Size(min=8,max=100) String password, @NotBlank @Size(max=100) String name) {}
    public record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {}
    public record AuthResponse(String token, UserResponse user) {}
    public record UserResponse(Long id, String email, String name) {}
    public record UpdateProfileRequest(@NotBlank @Size(max=100) String name) {}
}
