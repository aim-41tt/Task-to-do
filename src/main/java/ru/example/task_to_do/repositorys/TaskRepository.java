package ru.example.task_to_do.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.example.task_to_do.entitys.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
