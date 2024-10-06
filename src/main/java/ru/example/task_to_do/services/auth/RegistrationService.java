package ru.example.task_to_do.services.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ru.example.task_to_do.entitys.Role;
import ru.example.task_to_do.entitys.User;
import ru.example.task_to_do.repositorys.UserRepository;
import ru.example.task_to_do.tdo.user.RegisterRequest;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class RegistrationService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public RegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public CompletableFuture<String> register(RegisterRequest request) {
		Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
		if (existingUser.isPresent()) {
			throw new IllegalArgumentException("Username is already taken");
		}

		User newUser = new User();
		newUser.setUsername(request.getUsername());
		newUser.setPassword(passwordEncoder.encode(request.getPassword()));
		newUser.setEmail(request.getEmail());
		newUser.setRole(Role.USER);

		userRepository.save(newUser);

		return CompletableFuture.completedFuture("User registered successfully");
	}
}
