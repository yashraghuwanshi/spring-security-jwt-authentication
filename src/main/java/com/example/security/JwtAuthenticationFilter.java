package com.example.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.example.service.CustomUserDetailsService;
import com.example.util.JwtTokenUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenUtil jwtTokenUtil;

	private final CustomUserDetailsService customUserDetailsService;

	private final HandlerExceptionResolver handlerExceptionResolver;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = null;
		String username = null;

		String header = request.getHeader("Authorization");

		try {
			if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
				token = header.substring(7, header.length());
				username = jwtTokenUtil.getUsernameFromToken(token);
			}

			if (StringUtils.hasText(username) && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

				if (jwtTokenUtil.validateToken(token, userDetails)) {

					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
			filterChain.doFilter(request, response);
		} catch (Exception ex) {
			handlerExceptionResolver.resolveException(request, response, null, ex);
		}
	}

}
