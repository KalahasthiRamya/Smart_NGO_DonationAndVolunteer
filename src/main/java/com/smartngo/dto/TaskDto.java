package com.smartngo.dto;

import com.smartngo.enums.TaskPriority;
import com.smartngo.enums.TaskStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {
    private String title;
    private String description;
    private Long campaignId;
    private Long volunteerId;
    private LocalDate startDate;
    private LocalDate dueDate;
    private TaskPriority priority;
    private TaskStatus status;
}
