package com.example.service;

import com.example.payload.LoginRequest;
import com.example.payload.RefreshTokenRequest;

public interface AuthService {
	
	String login(LoginRequest loginRequest);
	
	String refreshToken(RefreshTokenRequest refreshTokenRequest);

}
