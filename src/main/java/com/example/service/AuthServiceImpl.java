package com.example.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.model.RefreshToken;
import com.example.payload.LoginRequest;
import com.example.payload.RefreshTokenRequest;
import com.example.util.JwtTokenUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

	private final AuthenticationManager authenticationManager;

	private final JwtTokenUtil jwtTokenUtil;

	private final RefreshTokenService refreshTokenService;

	@Override
	public String login(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		if (authentication.isAuthenticated()) {
			SecurityContext securityContext = SecurityContextHolder.getContext();
			securityContext.setAuthentication(authentication);
			String token = jwtTokenUtil.generateToken(authentication.getName());
			return token;
		} else {
			throw new UsernameNotFoundException("Invalid Username/Password");
		}

	}

	@Override
	public String refreshToken(RefreshTokenRequest refreshTokenRequest) {
		return refreshTokenService.findByTokenId(refreshTokenRequest.getTokenId())
				.map(refreshTokenService::verifyExpiration).map(RefreshToken::getUser).map(user -> {
					return jwtTokenUtil.generateToken(user.getUsername());
				}).orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
	}

}
