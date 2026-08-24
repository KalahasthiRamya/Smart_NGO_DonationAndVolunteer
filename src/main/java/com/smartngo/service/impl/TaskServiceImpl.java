package com.smartngo.service.impl;

import com.smartngo.dto.TaskDto;
import com.smartngo.entity.Campaign;
import com.smartngo.entity.Task;
import com.smartngo.entity.Volunteer;
import com.smartngo.enums.NotificationType;
import com.smartngo.enums.TaskPriority;
import com.smartngo.enums.TaskStatus;
import com.smartngo.exception.ResourceNotFoundException;
import com.smartngo.repository.CampaignRepository;
import com.smartngo.repository.TaskRepository;
import com.smartngo.repository.VolunteerRepository;
import com.smartngo.service.NotificationService;
import com.smartngo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public Task createTask(TaskDto dto) {
        Campaign campaign = null;
        if (dto.getCampaignId() != null) {
            campaign = campaignRepository.findById(dto.getCampaignId()).orElse(null);
        }

        Volunteer volunteer = null;
        if (dto.getVolunteerId() != null) {
            volunteer = volunteerRepository.findById(dto.getVolunteerId()).orElse(null);
        }

        Task task = Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .campaign(campaign)
                .assignedVolunteer(volunteer)
                .startDate(dto.getStartDate())
                .dueDate(dto.getDueDate())
                .priority(dto.getPriority() != null ? dto.getPriority() : TaskPriority.MEDIUM)
                .status(dto.getStatus() != null ? dto.getStatus() : TaskStatus.ASSIGNED)
                .build();

        Task savedTask = taskRepository.save(task);

        if (volunteer != null && volunteer.getUser() != null) {
            notificationService.sendNotification(
                    volunteer.getUser(),
                    NotificationType.TASK,
                    "You have been assigned a new task: " + task.getTitle()
            );
        }

        return savedTask;
    }

    @Override
    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public List<Task> findByVolunteer(Volunteer volunteer) {
        return taskRepository.findByAssignedVolunteer(volunteer);
    }

    @Override
    @Transactional
    public Task updateTaskStatus(Long id, TaskStatus status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        task.setStatus(status);
        Task updated = taskRepository.save(task);

        if (status == TaskStatus.COMPLETED && task.getAssignedVolunteer() != null && task.getAssignedVolunteer().getUser() != null) {
            notificationService.sendNotification(
                    task.getAssignedVolunteer().getUser(),
                    NotificationType.TASK,
                    "Great job! Task marked as COMPLETED: " + task.getTitle()
            );
        }

        return updated;
    }

    @Override
    @Transactional
    public Task assignVolunteer(Long taskId, Long volunteerId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        Volunteer volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found with id: " + volunteerId));

        task.setAssignedVolunteer(volunteer);
        Task updated = taskRepository.save(task);

        if (volunteer.getUser() != null) {
            notificationService.sendNotification(
                    volunteer.getUser(),
                    NotificationType.TASK,
                    "You have been assigned to task: " + task.getTitle()
            );
        }

        return updated;
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    @Override
    public long countCompletedTasks() {
        return taskRepository.countByStatus(TaskStatus.COMPLETED);
    }

    @Override
    public List<Task> findUpcomingTasks() {
        return taskRepository.findTop5ByOrderByDueDateAsc();
    }
}
