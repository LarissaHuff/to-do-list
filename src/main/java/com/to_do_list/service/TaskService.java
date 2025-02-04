package com.to_do_list.service;

import com.to_do_list.dto.TaskDto;
import com.to_do_list.exception.BusinessException;
import com.to_do_list.model.Task;
import com.to_do_list.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

        return taskRepository.save(task);

    }

    public Task findById(UUID id) {
        return taskRepository.findById(id).orElseThrow(() -> new BusinessException("Task not found"));
    }

    public Task update(UUID id, TaskDto taskDto) {
        Task task = findById(id);
        task.setName(taskDto.name());
        task.setPriority(taskDto.priority());
        task.setDescription(taskDto.description());

        return taskRepository.save(task);
    }

    public Task completeTask(UUID id) {
        Task task = findById(id);
        task.setDone(LocalDate.now());

        return taskRepository.save(task);
    }

    public void delete(UUID id){
        taskRepository.deleteById(id);
    }
}
