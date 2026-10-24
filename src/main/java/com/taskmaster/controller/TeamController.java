package com.taskmaster.controller;
import com.taskmaster.dto.TeamDtos.*;
import com.taskmaster.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/teams") @RequiredArgsConstructor
public class TeamController {
    private final TeamService service;
    @PostMapping TeamResponse create(@Valid @RequestBody CreateTeamRequest r){return service.create(r);}
    @GetMapping List<TeamResponse> mine(){return service.mine();}
    @PostMapping("/{id}/members") TeamResponse invite(@PathVariable Long id,@RequestParam @jakarta.validation.constraints.Email String email){return service.addMember(id,email);}
}
