package com.example.advice;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		//response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
		//response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		//response.getWriter().write("Unauthorized");
		
		// Forward the AuthenticationException to the global exception handler
		throw authException;

	}

}