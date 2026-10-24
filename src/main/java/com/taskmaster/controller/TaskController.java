package com.taskmaster.controller;
import com.taskmaster.dto.TaskDtos.*;
import com.taskmaster.entity.TaskStatus;
import com.taskmaster.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/tasks") @RequiredArgsConstructor
public class TaskController {
    private final TaskService service;
    @PostMapping TaskResponse create(@Valid @RequestBody CreateTaskRequest r){return service.create(r);}
    @GetMapping Page<TaskResponse> search(@RequestParam Long teamId,@RequestParam(required=false) String search,@RequestParam(required=false) TaskStatus status,@RequestParam(required=false) Long assigneeId,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size,@RequestParam(defaultValue="createdAt") String sort){return service.search(teamId,search,status,assigneeId,page,size,sort);}
    @PutMapping("/{id}") TaskResponse update(@PathVariable Long id,@Valid @RequestBody UpdateTaskRequest r){return service.update(id,r);}
    @DeleteMapping("/{id}") ResponseEntity<Void> delete(@PathVariable Long id){service.delete(id);return ResponseEntity.noContent().build();}
    @PostMapping("/{id}/comments") CommentResponse comment(@PathVariable Long id,@Valid @RequestBody CommentRequest r){return service.comment(id,r);}
    @PostMapping("/{id}/attachments") AttachmentResponse attach(@PathVariable Long id,@Valid @RequestBody AttachmentRequest r){return service.attach(id,r);}
}
