package ru.example.task_to_do.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.example.task_to_do.entitys.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
	boolean existsByUsername(String username);
}
