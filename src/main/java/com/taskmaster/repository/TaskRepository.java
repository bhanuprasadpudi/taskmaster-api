package com.taskmaster.repository;
import com.taskmaster.entity.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("select t from Task t where t.team.id = :teamId and (:search is null or lower(t.title) like lower(concat('%',:search,'%')) or lower(coalesce(t.description,'')) like lower(concat('%',:search,'%'))) and (:status is null or t.status = :status) and (:assigneeId is null or t.assignee.id = :assigneeId)")
    Page<Task> search(@Param("teamId") Long teamId, @Param("search") String search, @Param("status") TaskStatus status, @Param("assigneeId") Long assigneeId, Pageable pageable);
}
