package com.example.tm.repository;

import com.example.tm.entity.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Task getTasksById(Long id);
    Task findFirstByOrderByIdDesc();
    List<Task> findTasksByCreaterId(Long id);
    List<Task> findTaskByCreaterId(Long id, Pageable pageable);
}
