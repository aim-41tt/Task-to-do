package ru.example.task_to_do.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.example.task_to_do.entitys.Task;
import ru.example.task_to_do.entitys.User;
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
	public CompletableFuture<Task> findTaskById(Long taskId, UserDetails userDetails) {
	    return CompletableFuture.supplyAsync(() -> {
	        return userRep.findByUsernameWithTasks(userDetails.getUsername())
	            .flatMap(user -> {
	                return user.getTasks().parallelStream()
	                    .filter(task -> task.getId().equals(taskId))  
	                    .findFirst();
	            }).get();
	    });
	}

	@Async
	public CompletableFuture<List<Task>> findTasksByUser(UserDetails userDetails) {
		return CompletableFuture.supplyAsync(() -> {
			return userRep.findByUsernameWithTasks(userDetails.getUsername())
					.map(User::getTasks)
					.orElseGet(ArrayList::new);
		});
	}

	@Async
	public CompletableFuture<Boolean> delTaskForUser(Task task , UserDetails userDetails) {
		return CompletableFuture.supplyAsync(() -> {
			Optional<User> userOpt = userRep.findByUsernameWithTasks(userDetails.getUsername());
			if(userOpt.isPresent()) {
				User user = userOpt.get();
				Optional<Task> taskOpt = user.getTasks()
						.parallelStream()
						.filter(t -> t.getId().equals(task.getId()))
						.findFirst();
				if(taskOpt.isPresent()) {
					return taskRep.deleteTaskById(taskOpt.get().getId());
				}
			}
			return false;
		});
	}
	
	@Async
	public CompletableFuture<Boolean> updateTaskForUser(Task task, UserDetails userDetails) {
	    if (task == null || task.getId() == null) {
	        return CompletableFuture.completedFuture(false);
	    }
	    
	    return CompletableFuture.supplyAsync(() -> {
	        Optional<User> userOpt = userRep.findByUsernameWithTasks(userDetails.getUsername());

	        if (userOpt.isPresent()) {
	            User user = userOpt.get();
	            Optional<Task> taskFromUser = user.getTasks()
	            		.parallelStream()
	            		.filter(t -> t.getId().equals(task.getId()))
	            		.findFirst();

	            if (taskFromUser.isPresent()) {
	                Task existingTask = taskFromUser.get();
	                existingTask.setTask(task);
	                taskRep.save(existingTask);
	                return true;
	            }
	        }
	        return false;
	    });
	}

}
