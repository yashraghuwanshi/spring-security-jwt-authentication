package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Role;
import com.example.model.UserRole;

public interface RoleRepostiory extends JpaRepository<Role, Long> {
	Optional<Role> findByName(UserRole name);
}
