package com.example.advice;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleSecurityException(Exception ex, HttpServletRequest request) {

		Map<String, Object> errorResponse = new HashMap<>();

		if (ex instanceof BadCredentialsException) {

			// Log the exception
			System.out.println("BadCredentialsException caught in GlobalExceptionHandler");

			errorResponse.put("timestamp", LocalDate.now());
			errorResponse.put("status", 401);
			errorResponse.put("message", ex.getMessage());
			errorResponse.put("exception_type", ex.getClass().getSimpleName());
			errorResponse.put("instance", request.getRequestURI());
			errorResponse.put("error", "Authentication failure!");

			return ResponseEntity.status(HttpStatus.valueOf(401)).body(errorResponse);
		}

		if (ex instanceof AuthenticationException) {

			// Log the exception
			System.out.println("AuthenticationException caught in GlobalExceptionHandler");

			errorResponse.put("timestamp", LocalDate.now());
			errorResponse.put("status", 401);
			errorResponse.put("message", ex.getMessage());
			errorResponse.put("exception_type", ex.getClass().getSimpleName());
			errorResponse.put("instance", request.getRequestURI());
			errorResponse.put("error", "Authentication failure!");

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}

		if (ex instanceof AccessDeniedException) {

			// Log the exception
			System.out.println("AccessDeniedException caught in GlobalExceptionHandler");

			errorResponse.put("timestamp", LocalDate.now());
			errorResponse.put("status", HttpStatus.FORBIDDEN.value());
			errorResponse.put("message", ex.getMessage());
			errorResponse.put("exception_type", ex.getClass().getSimpleName());
			errorResponse.put("instance", request.getRequestURI());
			errorResponse.put("error", "Not authorized!");

			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
		}

		if (ex instanceof SignatureException) {

			// Log the exception
			System.out.println("SignatureException caught in GlobalExceptionHandler");

			errorResponse.put("timestamp", LocalDate.now());
			errorResponse.put("status", HttpStatus.valueOf(403));
			errorResponse.put("message", ex.getMessage());
			errorResponse.put("exception_type", ex.getClass().getSimpleName());
			errorResponse.put("instance", request.getRequestURI());
			errorResponse.put("error", "JWT signature not valid!");

			return ResponseEntity.status(HttpStatus.valueOf(403)).body(errorResponse);
		}

		if (ex instanceof ExpiredJwtException) {

			// Log the exception
			System.out.println("ExpiredJwtException caught in GlobalExceptionHandler");

			errorResponse.put("timestamp", LocalDate.now());
			errorResponse.put("status", HttpStatus.FORBIDDEN);
			errorResponse.put("message", ex.getMessage());
			errorResponse.put("exception_type", ex.getClass().getSimpleName());
			errorResponse.put("instance", request.getRequestURI());
			errorResponse.put("error", "JWT token has expired!");

			return ResponseEntity.status(HttpStatus.valueOf(403)).body(errorResponse);
		}

		errorResponse.put("timestamp", LocalDate.now());
		errorResponse.put("status", 500); // Default to Internal Server Error
		errorResponse.put("error", "Internal Server Error");
		errorResponse.put("message", ex.getMessage());
		errorResponse.put("exception_type", ex.getClass().getSimpleName());
		errorResponse.put("instance", request.getRequestURI());

		return ResponseEntity.status((int) errorResponse.get("status")).body(errorResponse);
	}
}
