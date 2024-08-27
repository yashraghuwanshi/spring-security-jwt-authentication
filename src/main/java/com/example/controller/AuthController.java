package com.example.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.RefreshToken;
import com.example.model.User;
import com.example.payload.JwtAuthResponse;
import com.example.payload.LoginRequest;
import com.example.payload.PasswordReset;
import com.example.payload.RefreshTokenRequest;
import com.example.repository.UserRepository;
import com.example.service.AuthService;
import com.example.service.RefreshTokenService;
import com.example.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;
	private final UserService userService;
	private final RefreshTokenService refreshTokenService;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@GetMapping("/userlist")
	public ResponseEntity<List<User>> getUsersByToken() {
		List<User> users = userService.getUser();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginRequest loginRequest)
			throws AuthenticationException {
		String jwt = authService.authenticate(loginRequest);
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginRequest.getUsername());
		JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
		if (!jwt.isEmpty()) {
			jwtAuthResponse.setAccessToken(jwt);
			jwtAuthResponse.setAuthenticated(true);
			jwtAuthResponse.setTokenId(refreshToken.getTokenId());
		}
		return ResponseEntity.ok(jwtAuthResponse);
	}

	@PostMapping("/refreshToken")
	public ResponseEntity<JwtAuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
		String jwtToken = authService.refreshToken(refreshTokenRequest);
		JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
		jwtAuthResponse.setAccessToken(jwtToken);
		jwtAuthResponse.setTokenId(refreshTokenRequest.getTokenId());
		return ResponseEntity.ok(jwtAuthResponse);
	}

	@PostMapping("/change-password")
	public ResponseEntity<String> changePassword(@RequestBody PasswordReset passwordReset) {

		// Get the current authenticated user's username
		String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

		// Load the user from the database
		User user = userRepository.findByUsername(currentUsername).orElseThrow(
				() -> new UsernameNotFoundException("User does not exists with username" + currentUsername));

		// Verify if the old password matches the current password
		if (!passwordEncoder.matches(passwordReset.getOldPassword(), user.getPassword())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect.");
		}
		
		// Encode the new password
		String encodedNewPassword = passwordEncoder.encode(passwordReset.getNewPassword());

		// Update the password in the database
		user.setPassword(encodedNewPassword);
		userRepository.save(user);

		return ResponseEntity.ok("Password changed successfully!");
	}

}