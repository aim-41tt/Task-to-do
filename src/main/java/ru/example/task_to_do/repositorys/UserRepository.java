package ru.example.task_to_do.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ru.example.task_to_do.entitys.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	@Query("SELECT u FROM User u LEFT JOIN FETCH u.tasks WHERE u.username = :username")
	Optional<User> findByUsernameWithTasks(@Param("username") String username);
	Optional<User> findByUsername(String username);
	boolean existsByUsername(String username);
}
