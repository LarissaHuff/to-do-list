package com.to_do_list.repository;

import com.to_do_list.model.Task;
import com.to_do_list.model.TaskStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    @Query("select t from Task t where t.status = :status or :status = null")
    List<Task> findAllByStatus(TaskStatusEnum status);
}
