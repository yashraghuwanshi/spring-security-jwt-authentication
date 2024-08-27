package com.example.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.stereotype.Service;

import com.example.model.RefreshToken;
import com.example.payload.LoginRequest;
import com.example.payload.RefreshTokenRequest;
import com.example.util.JwtTokenUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("all")
@RequiredArgsConstructor
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

	private final AuthenticationManager authenticationManager;

	private final JwtTokenUtil jwtTokenUtil;

	private final RefreshTokenService refreshTokenService;

	@Override
	public String authenticate(LoginRequest loginRequest) {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				loginRequest.getUsername(), loginRequest.getPassword());
		
		log.info("Authentication Token Before Authentication: {}", authenticationToken);

		// Authenticate the user
		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		
		log.info("Authentication Token After Authentication: {}", authentication);

		// Set the authenticated Authentication object in the SecurityContext
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(authentication);

		// Retrieve the Authentication object from SecurityContext to verify
		Authentication auth = context.getAuthentication();
		
		log.info("Authentication Token in Security Context: {}", auth);

		if (authentication.isAuthenticated()) {
			System.out.println("User is Authenticated");

			/*
			 * User user = (User) authentication.getPrincipal();
			 * System.out.println(user.getUsername());
			 * System.out.println(user.getPassword());
			 * System.out.println(user.getAuthorities().toString());
			 * log.info("Credentials of Authenticated User: {}",
			 * authentication.getCredentials());
			 * log.info("Permissions of Authenticated User: {}",
			 * authentication.getAuthorities()); log.info("Name of Authenticated User: {}",
			 * authentication.getName());
			 */

			// Generate the token
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
