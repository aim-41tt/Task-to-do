package ru.example.task_to_do.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ru.example.task_to_do.entitys.Task;
import ru.example.task_to_do.services.TaskService;

import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/tasks")
public class TaskController {

	private final TaskService taskService;

	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	@GetMapping
	public CompletableFuture<String> getUserTasks(@AuthenticationPrincipal UserDetails user, Model model) {
		return taskService.findTasksByUser(user).thenApply(tasks -> {
			model.addAttribute("tasks", tasks);

			return "tasks";
		});
	}

	@GetMapping("/new")
	public String showCreateTaskForm(Model model) {
		model.addAttribute("task", new Task());
		return "create_task"; 
	}

	@PostMapping("/create")
	public String createTask(@ModelAttribute Task task, @AuthenticationPrincipal UserDetails userDetails) {
		taskService.saveTasks(task, userDetails);
		return "redirect:/tasks";
	}
}
