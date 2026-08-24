package com.smartngo.repository;

import com.smartngo.entity.Task;
import com.smartngo.entity.Volunteer;
import com.smartngo.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedVolunteer(Volunteer volunteer);
    List<Task> findByStatus(TaskStatus status);
    long countByStatus(TaskStatus status);
    List<Task> findTop5ByOrderByDueDateAsc();
}
