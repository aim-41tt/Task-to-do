package ru.example.task_to_do.services.auth;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.example.task_to_do.entitys.Task;
import ru.example.task_to_do.repositorys.TaskRepository;

@Service
@Transactional
public class TaskService {

	@Autowired
	private TaskRepository taskRep;

	@Async
	public CompletableFuture<List<Task>> getTasksForUser(long userId) {
		return CompletableFuture.supplyAsync(() -> taskRep.findByUser_id(userId)
				.orElseThrow(() -> new RuntimeException("Tasks not found for user")));
	}

	@Async
	public CompletableFuture<Boolean> delTaskForUser(Task task) {
		return CompletableFuture.supplyAsync(() -> {
			taskRep.delete(task);
			return true; 
		});
	}

	@Async
	public CompletableFuture<Boolean> updateTaskForUser(Task task) {
		if (task == null || task.getId() == null) {
			return CompletableFuture.completedFuture(false);
		}

		return CompletableFuture.supplyAsync(() -> {
			Optional<Task> optionalTask = taskRep.findById(task.getId());
			if (optionalTask.isPresent()) {
				Task existingTask = optionalTask.get();
				existingTask.setTask(existingTask);
				taskRep.save(existingTask);
				return true;
			} else {
				return false;
			}
		});
	}
}
