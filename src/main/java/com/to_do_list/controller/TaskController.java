package com.to_do_list.controller;

import com.to_do_list.dto.CompleteTaskDto;
import com.to_do_list.dto.TaskDto;
import com.to_do_list.dto.TaskViewDto;
import com.to_do_list.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    TaskViewDto create(@RequestBody TaskDto taskDto) {
        return new TaskViewDto(taskService.create(taskDto));
    }

    @GetMapping("/{id}")
    TaskViewDto findById(@PathVariable UUID id) {
        return new TaskViewDto(taskService.findById(id));
    }

    @PatchMapping("/{id}")
    CompleteTaskDto completeTask(@PathVariable UUID id) {
        return new CompleteTaskDto(taskService.completeTask(id));
    }

    @PutMapping("/{id}")
    TaskViewDto update(@PathVariable UUID id, @RequestBody TaskDto taskDto) {
        return new TaskViewDto(taskService.update(id, taskDto));
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable UUID id) {
        taskService.delete(id);
    }
}
