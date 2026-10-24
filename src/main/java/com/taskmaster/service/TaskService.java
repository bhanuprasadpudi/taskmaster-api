package com.taskmaster.service;
import com.taskmaster.dto.TaskDtos.*;
import com.taskmaster.entity.*;
import com.taskmaster.exception.ApiException;
import com.taskmaster.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service @RequiredArgsConstructor @Transactional
public class TaskService {
    private final TaskRepository tasks; private final UserRepository users; private final TeamService teams; private final CurrentUserService current; private final SimpMessagingTemplate notifications;
    public TaskResponse create(CreateTaskRequest r){User creator=current.get();Team team=teams.getMember(r.teamId());User assignee=resolveAssignee(r.assigneeId(),team);Task t=Task.builder().title(r.title()).description(r.description()).dueDate(r.dueDate()).creator(creator).assignee(assignee).team(team).build();TaskResponse out=dto(tasks.save(t));notify(team.getId(),out);return out;}
    public Page<TaskResponse> search(Long teamId,String search,TaskStatus status,Long assignee,int page,int size,String sort){Team team=teams.getMember(teamId);String property=Set.of("title","dueDate","createdAt","updatedAt","status").contains(sort)?sort:"createdAt";Pageable p=PageRequest.of(Math.max(page,0),Math.min(Math.max(size,1),100),Sort.by(Sort.Direction.DESC,property));return tasks.search(team.getId(),search==null||search.isBlank()?null:search.trim(),status,assignee,p).map(this::dto);}
    public TaskResponse update(Long id,UpdateTaskRequest r){Task t=get(id);User a=resolveAssignee(r.assigneeId(),t.getTeam());t.setTitle(r.title());t.setDescription(r.description());t.setStatus(r.status());t.setDueDate(r.dueDate());t.setAssignee(a);TaskResponse out=dto(tasks.save(t));notify(t.getTeam().getId(),out);return out;}
    public void delete(Long id){tasks.delete(get(id));}
    public Task get(Long id){Task t=tasks.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Task not found"));teams.getMember(t.getTeam().getId());return t;}
    public CommentResponse comment(Long id,CommentRequest r){Task t=get(id);Comment saved=Comment.builder().task(t).author(current.get()).content(r.content()).build();t.getComments().add(saved);tasks.save(t);return new CommentResponse(saved.getId(),saved.getContent(),saved.getAuthor().getId(),saved.getAuthor().getName(),saved.getCreatedAt());}
    public AttachmentResponse attach(Long id,AttachmentRequest r){Task t=get(id);Attachment a=Attachment.builder().task(t).uploadedBy(current.get()).fileName(r.fileName()).storageUrl(r.storageUrl()).fileSize(r.fileSize()).contentType(r.contentType()).build();t.getAttachments().add(a);tasks.save(t);return new AttachmentResponse(a.getId(),a.getFileName(),a.getStorageUrl(),a.getFileSize(),a.getContentType(),a.getUploadedBy().getId(),a.getCreatedAt());}
    private User resolveAssignee(Long id,Team team){if(id==null)return null;return team.getMembers().stream().filter(m->m.getId().equals(id)).findFirst().orElseThrow(()->new ApiException(HttpStatus.BAD_REQUEST,"Assignee must be a team member"));}
    private TaskResponse dto(Task t){return new TaskResponse(t.getId(),t.getTitle(),t.getDescription(),t.getStatus(),t.getDueDate(),t.getCreator().getId(),t.getAssignee()==null?null:t.getAssignee().getId(),t.getTeam().getId(),t.getCreatedAt(),t.getUpdatedAt());}
    private void notify(Long teamId,TaskResponse task){notifications.convertAndSend("/topic/teams/"+teamId+"/tasks",task);}
}
