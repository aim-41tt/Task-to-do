package ru.example.task_to_do.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("task")
public class TaskController {

	@GetMapping("{id_task}")
	public String getMethodName() {
		return new String();
	}
	

}
