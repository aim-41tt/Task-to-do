package ru.example.task_to_do.controllers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.example.task_to_do.services.auth.RegistrationService;
import ru.example.task_to_do.tdo.user.RegisterRequest;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.concurrent.CompletableFuture;

@Controller
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @GetMapping("/reg")
    public String register() {
        return "registration";
    }

    @PostMapping("/reg")
    public String registerUser(@RequestParam("username") String username, 
                               @RequestParam("password") String password,
                               @RequestParam("email") String email, Model model) {

        if (username == null || username.isEmpty() || password == null || password.isEmpty() || email == null
                || email.isEmpty()) {
            model.addAttribute("errorMessage", "Все поля должны быть заполнены!");
            return "registration";
        }

        RegisterRequest user = new RegisterRequest(username, password, email);

        // Выполняем асинхронный вызов
        CompletableFuture<String> futureMessage = registrationService.register(user);

        futureMessage.thenAccept(message -> {
            model.addAttribute("errorMessage", message);
        }).exceptionally(ex -> {
            model.addAttribute("errorMessage", "Ошибка регистрации!");
            return null;
        });

        return "redirect:/login";
    }
}
