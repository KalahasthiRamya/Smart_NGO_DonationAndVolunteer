package com.smartngo.service;

import com.smartngo.dto.TaskDto;
import com.smartngo.entity.Task;
import com.smartngo.entity.Volunteer;
import com.smartngo.enums.TaskStatus;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    Task createTask(TaskDto dto);
    List<Task> findAllTasks();
    Optional<Task> findById(Long id);
    List<Task> findByVolunteer(Volunteer volunteer);
    Task updateTaskStatus(Long id, TaskStatus status);
    Task assignVolunteer(Long taskId, Long volunteerId);
    void deleteTask(Long id);
    long countCompletedTasks();
    List<Task> findUpcomingTasks();
}
