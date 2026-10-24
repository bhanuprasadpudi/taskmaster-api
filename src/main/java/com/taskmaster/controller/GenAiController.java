package com.taskmaster.controller;
import com.taskmaster.dto.GenAiDtos.*;
import com.taskmaster.service.GenAiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/genai") @RequiredArgsConstructor
public class GenAiController {
    private final GenAiService service;
    @PostMapping("/task-description") GenerateResponse generate(@Valid @RequestBody GenerateRequest r){return new GenerateResponse(service.generate(r));}
}
