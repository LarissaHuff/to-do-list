package com.to_do_list.controller;

import com.to_do_list.dto.TaskDto;
import com.to_do_list.dto.TaskViewDto;
import com.to_do_list.model.Priority;
import com.to_do_list.model.Task;
import com.to_do_list.model.TaskStatusEnum;
import com.to_do_list.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/pending")
    List<TaskViewDto> getPendingTasks() {
        List<Task> tasks = taskService.getPendingTasks();

        return tasks.stream()
                .map(TaskViewDto::new)
                .toList();
    }

    @GetMapping("/priority/{priority}")
    List<TaskViewDto> getAllTasksByPriority(@PathVariable Priority priority) {
        List<Task> tasks = taskService.getAllTasksByPriority(priority);

        return tasks.stream()
                .map(TaskViewDto::new)
                .toList();
    }

    @PatchMapping("/{id}")
    TaskViewDto completeTask(@PathVariable UUID id) {
        return new TaskViewDto(taskService.completeTask(id));
    }

    //Receber uma lista de status talvez
    @GetMapping
    List<TaskViewDto> findAll(@RequestParam(required = false) TaskStatusEnum taskStatus) {
        List<Task> taskList = taskService.findAll(taskStatus);
        return taskList.stream()
                .map(TaskViewDto::new)
                .toList();
    }

    @PatchMapping("{id}/start")
    public TaskViewDto start(@PathVariable UUID id) {
        return new TaskViewDto(taskService.start(id));
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
