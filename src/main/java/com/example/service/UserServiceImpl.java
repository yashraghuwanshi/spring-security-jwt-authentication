package com.example.service;

import java.util.Collections;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.model.Role;
import com.example.model.User;
import com.example.model.UserRole;
import com.example.payload.SignupRequest;
import com.example.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void saveUser(SignupRequest signupRequest) {

		if (userRepository.existsByUsername(signupRequest.getUsername())) {
			throw new IllegalArgumentException("Username is already taken!");
		}

		// add check for email exists in DB
		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			throw new IllegalArgumentException("Email is already taken!");
		}
		
		User user = new User();
		user.setName(signupRequest.getName());
		user.setEmail(signupRequest.getEmail());
		user.setUsername(signupRequest.getUsername());
		user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
		
		Role role = new Role();
		role.setName(UserRole.ROLE_USER);

		user.setRoles(Collections.singleton(role));
		userRepository.save(user);

	}

	@Override
	public List<User> getUser() {
		return userRepository.findAll();
	}

	@Override
	public void deleteUser(long id) {
		userRepository.deleteById(id);
	}
	
	

}
