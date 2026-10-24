package com.taskmaster.service;
import com.taskmaster.dto.TeamDtos.*;
import com.taskmaster.entity.*;
import com.taskmaster.exception.ApiException;
import com.taskmaster.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor @Transactional
public class TeamService {
    private final TeamRepository teams; private final UserRepository users; private final CurrentUserService current;
    public TeamResponse create(CreateTeamRequest r){User u=current.get();Team t=Team.builder().name(r.name()).description(r.description()).owner(u).build();t.getMembers().add(u);return dto(teams.save(t));}
    public List<TeamResponse> mine(){return teams.findByMemberId(current.get().getId()).stream().map(this::dto).toList();}
    public TeamResponse addMember(Long id,String email){Team t=getMember(id);User requester=current.get();if(!t.getOwner().getId().equals(requester.getId()))throw new ApiException(HttpStatus.FORBIDDEN,"Only the team owner can invite members");User u=users.findByEmailIgnoreCase(email).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Invitee not found"));t.getMembers().add(u);return dto(teams.save(t));}
    public Team getMember(Long id){Team t=teams.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Team not found"));if(t.getMembers().stream().noneMatch(m->m.getId().equals(current.get().getId())))throw new ApiException(HttpStatus.FORBIDDEN,"You are not a member of this team");return t;}
    private TeamResponse dto(Team t){return new TeamResponse(t.getId(),t.getName(),t.getDescription(),t.getOwner().getId(),t.getMembers().size());}
}
