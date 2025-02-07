package com.to_do_list.dto;

import com.to_do_list.model.Priority;
import com.to_do_list.model.Task;
import com.to_do_list.model.TaskStatusEnum;

import java.time.LocalDate;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Optional.ofNullable;

public record TaskViewDto(UUID id, String name, String description,
                          Priority priority, TaskStatusEnum status,
                          LocalDate created, LocalDate started, LocalDate done, Long duration) {
    public TaskViewDto(Task task) {
        this(task.getId(), task.getName(), task.getDescription(),
                task.getPriority(), task.getStatus(), task.getCreated(),
                task.getStarted(), task.getDone(), durationTime(task));
    }

    public static Long durationTime(Task task) {
        LocalDate started = task.getStarted();
        LocalDate done = task.getDone();

        return ofNullable(started)
                .map(startDate -> ofNullable(done).orElse(LocalDate.now()))
                .map(endDate -> DAYS.between(started, endDate))
                .orElse(0L);

        /*if (started != null) {
            LocalDate endDate;
            if (done != null) {
                endDate = done;
            } else {
                endDate = LocalDate.now();
            }
            return DAYS.between(started, endDate);
        }
        return 0L;*/
    }
}
