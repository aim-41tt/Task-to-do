package ru.example.task_to_do.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ru.example.task_to_do.entitys.Task;
import ru.example.task_to_do.entitys.User;
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
	
	 // Отображение формы для создания задачи
    @GetMapping("/new")
    public String showCreateTaskForm(Model model) {
        model.addAttribute("task", new Task()); // Добавляем пустой объект Task для формы
        return "create_task"; // Верните имя HTML-шаблона для формы
    }

    // Обработка формы создания задачи
//    @PostMapping("/create")
//    public String createTask(@ModelAttribute Task task, @AuthenticationPrincipal UserDetails userDetails) {
//        String username = userDetails.getUsername();
//
//        // Здесь вы должны получить userId по имени пользователя
//        // Например:
//        Long userId = 
//        
//        task.setUserId(userId); // Устанавливаем userId для задачи
//        taskService.saveTask(task); // Сохраняем задачу через сервис
//
//        return "redirect:/tasks"; // Перенаправление на страницу со списком задач
//    }
}
