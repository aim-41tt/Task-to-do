package ru.example.task_to_do.repositorys;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.example.task_to_do.entitys.Task;
import ru.example.task_to_do.entitys.User;

public interface TaskRepository extends JpaRepository<Task, Long> {
	public Optional<List<Task>> findByUser_id(User user_id);
	public Optional<List<Task>> findByUser(User user);
	default boolean deleteTaskById(Long id) {
		if (existsById(id)) {
			deleteById(id); 
			return true;
		}
		return false;
	}
}
