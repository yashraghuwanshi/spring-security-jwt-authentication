package com.example.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.User;
import com.example.payload.SignupRequest;
import com.example.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController {

	private final UserService userService;
	
	@PostMapping
	public ResponseEntity<String> saveUser(@RequestBody SignupRequest signupRequest) {
		userService.saveUser(signupRequest);
		return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED); 
	}
	
	@GetMapping
	public ResponseEntity<List<User>> getUser() {
		List<User> users = userService.getUser();
		return new ResponseEntity<>(users, HttpStatus.OK); 
	}
	
}
