package com.moviebooking.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.moviebooking.auth.model.User;

public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByUsername(String username);
	Optional<User> findByPassword(String password);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
	
}