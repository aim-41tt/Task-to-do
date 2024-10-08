package ru.example.task_to_do.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ru.example.task_to_do.entitys.Task;
import ru.example.task_to_do.services.TaskService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

	@GetMapping("/update/{id}")
	public CompletableFuture<String> updateTaskForm(@PathVariable Long id,
			@AuthenticationPrincipal UserDetails userDetails, Model model) {

		return CompletableFuture.supplyAsync(() -> {

			try {
				model.addAttribute("task", taskService.findTaskById(id, userDetails).get());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "updateTask";
		});
	}

	@PostMapping("/update/{id}")
	public CompletableFuture<String> updateTask(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails,
			@ModelAttribute Task task) {

		return CompletableFuture.supplyAsync(() -> {
			taskService.updateTaskForUser(task, userDetails);
			return "redirect:/tasks";
		});
	}

	@PostMapping("/del/{id}")
	public CompletableFuture<String> postMethodName(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, @ModelAttribute Task task) {
		return CompletableFuture.supplyAsync(()->{
			if (!taskService.delTaskForUser(task, userDetails).join()) {
				System.out.println("error del Task");
				return "redirect:/tasks";
			}
			return "redirect:/tasks";
		});
	}

}
