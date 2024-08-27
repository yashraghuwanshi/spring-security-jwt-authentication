package com.example.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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

	@PostMapping("/save")
	public ResponseEntity<String> saveUser(@RequestBody SignupRequest signupRequest) {
		userService.saveUser(signupRequest);
		return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
	}

	@GetMapping("/get")
	public ResponseEntity<List<User>> getUser() {
		List<User> users = userService.getUser();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	//@ApiOperation(value = "delete user", hidden = true) 
	@Deprecated
	@DeleteMapping("/delete")
	public ResponseEntity<Void> getUserById(@RequestParam("id") long id) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}

	@ResponseStatus(code = HttpStatus.OK)
	@GetMapping("/username")
	public String getUser(Principal principal) {
		String name = principal.getName();
		return "Current User: " + name;
	}

	@ResponseStatus(code = HttpStatus.OK)
	@GetMapping("/user-details")
	public UserDetails getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
		return userDetails;
	}

}
