package ru.example.task_to_do.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.example.task_to_do.entitys.Task;
import ru.example.task_to_do.repositorys.TaskRepository;
import ru.example.task_to_do.repositorys.UserRepository;

@Service
@Transactional
public class TaskService {

	private TaskRepository taskRep;
	private UserRepository userRep; 

	
	
	public TaskService(TaskRepository taskRep, UserRepository userRep) {
		this.taskRep = taskRep;
		this.userRep = userRep;
	}

	@Async
	public CompletableFuture<Task> saveTasks(Task task, UserDetails userDetails) {
		task.setUser(userRep.findByUsername(userDetails.getUsername()).get());
		return CompletableFuture.supplyAsync(() -> taskRep.save(task));
	}
	
	@Async
	public CompletableFuture<List<Task>> findTasksByUser(UserDetails user) {
		return CompletableFuture.supplyAsync(() -> taskRep.findByUser(userRep.findByUsername(user.getUsername()).get())
				.orElseGet(ArrayList::new));
	}

	@Async
	public CompletableFuture<Boolean> delTaskForUser(Task task) {
		return CompletableFuture.supplyAsync(() -> {
			return taskRep.deleteTaskById(task.getId());
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
