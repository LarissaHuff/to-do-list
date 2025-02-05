package com.to_do_list.dto;

import com.to_do_list.model.Priority;
import com.to_do_list.model.Task;
import com.to_do_list.model.TaskStatusEnum;

import java.time.LocalDate;
import java.util.UUID;

public record TaskViewDto(UUID id, String name, String description,
                          Priority priority, TaskStatusEnum status,
                          LocalDate created, LocalDate started, LocalDate done) {
    public TaskViewDto(Task task) {
        this(task.getId(), task.getName(), task.getDescription(),
                task.getPriority(), task.getStatus(), task.getCreated(), task.getStarted(), task.getDone());
    }
}
