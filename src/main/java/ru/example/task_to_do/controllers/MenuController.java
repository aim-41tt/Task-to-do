package ru.example.task_to_do.controllers;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MenuController {

	@GetMapping
	public CompletableFuture<String> getMenu() {
		 return CompletableFuture.supplyAsync(() -> "menu");
	}
	
	
	
}
