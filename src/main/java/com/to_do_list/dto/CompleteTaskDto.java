package com.to_do_list.dto;

import com.to_do_list.model.Priority;
import com.to_do_list.model.Task;

import java.time.LocalDate;
import java.util.UUID;

public record CompleteTaskDto(UUID id, String name, String description,
                              Priority priority, LocalDate created, LocalDate done) {
    public CompleteTaskDto(Task task) {
        this(task.getId(), task.getName(), task.getDescription(),
                task.getPriority(), task.getCreated(), task.getDone());
    }
}
