package ru.example.task_to_do.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/menu")
public class MenuController {

	@GetMapping
	public String getMenu() {
		return "menu";
	}
	
	@PostMapping
	public String postMethodName() {
		return "text";
	}
	
	
}
