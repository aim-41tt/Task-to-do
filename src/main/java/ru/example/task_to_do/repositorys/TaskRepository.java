package ru.example.task_to_do.repositorys;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.example.task_to_do.entitys.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
	public Optional<List<Task>> findByUser_id(long user_id);
	public Optional<Boolean> deleteByTask(Task task);

}
