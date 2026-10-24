package com.taskmaster.controller;
import com.taskmaster.dto.AuthDtos.*;
import com.taskmaster.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController {
    private final AuthService service;
    @PostMapping("/register") ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(service.register(r));}
    @PostMapping("/login") AuthResponse login(@Valid @RequestBody LoginRequest r){return service.login(r);}
    @GetMapping("/me") UserResponse me(){return service.profile();}
    @PutMapping("/me") UserResponse update(@Valid @RequestBody UpdateProfileRequest r){return service.update(r);}
}
