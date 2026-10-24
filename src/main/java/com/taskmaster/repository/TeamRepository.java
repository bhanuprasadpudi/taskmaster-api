package com.taskmaster.repository;
import com.taskmaster.entity.Team;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.*;
public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("select t from Team t join t.members m where m.id = :userId") List<Team> findByMemberId(@Param("userId") Long userId);
}
