package com.to_do_list.service;

import com.to_do_list.dto.TaskDto;
import com.to_do_list.exception.BusinessException;
import com.to_do_list.model.Priority;
import com.to_do_list.model.Task;
import com.to_do_list.model.TaskStatusEnum;
import com.to_do_list.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task create(TaskDto taskDto) {
        Task task = new Task();
        task.setName(taskDto.name());
        task.setDescription(taskDto.description());
        task.setPriority(taskDto.priority());
        task.setCreated(LocalDate.now());
        task.setStatus(TaskStatusEnum.CREATED);

        return taskRepository.save(task);

    }

    public Task findById(UUID id) {
        return taskRepository.findById(id).orElseThrow(() -> new BusinessException("Task not found"));
    }


    public List<Task> getPendingTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .filter(it -> it.getDone() == null)
                .sorted(Comparator.comparing(Task::getPriority))
                .toList();
    }

    public List<Task> getAllTasksByPriority(Priority priority) {
        List<Task> tasks = taskRepository.findAll();

        return tasks.stream()
                .filter(it -> it.getPriority().equals(priority))
                .toList();
    }

    public Task update(UUID id, TaskDto taskDto) {
        Task task = findById(id);
        task.setName(taskDto.name());
        task.setPriority(taskDto.priority());
        task.setDescription(taskDto.description());

        return taskRepository.save(task);
    }

    public Task start(UUID id) {
        Task task = findById(id);
        task.setStarted(LocalDate.now());
        task.setDone(null);
        task.setStatus(TaskStatusEnum.STARTED);

        return taskRepository.save(task);
    }

    public Task completeTask(UUID id) {
        Task task = findById(id);
        if (task.getDone() != null) {
            throw new BusinessException("Task already completed.");
        }
        task.setDone(LocalDate.now());
        task.setStatus(TaskStatusEnum.DONE);
        return taskRepository.save(task);
    }

    public void delete(UUID id) {
        taskRepository.deleteById(id);
    }

    public List<Task> findAll(TaskStatusEnum taskStatus) {
        List<Task> tasks = taskRepository.findAllByStatus(taskStatus);
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getPriority))
                .toList();
    }
}
