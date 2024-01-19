package com.example.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.RefreshToken;
import com.example.model.User;
import com.example.payload.JwtAuthResponse;
import com.example.payload.LoginRequest;
import com.example.payload.RefreshTokenRequest;
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

	@GetMapping("/userlist")
	public ResponseEntity<List<User>> getUsersByToken() {
		List<User> users = userService.getUser();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> authenticate(@RequestBody LoginRequest loginRequest)
			throws AuthenticationException {
		String token = authService.login(loginRequest);
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginRequest.getUsername());
		JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
		jwtAuthResponse.setAccessToken(token);
		jwtAuthResponse.setTokenId(refreshToken.getTokenId());
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

}