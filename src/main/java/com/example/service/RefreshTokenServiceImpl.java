package com.example.service;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.model.RefreshToken;
import com.example.model.User;
import com.example.repository.RefreshTokenRepository;
import com.example.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

	@Value("${app.jwtRefreshExpirationMs}")
	private Long jwtRefreshExpirationMs;

	private final RefreshTokenRepository refreshTokenRepository;

	private final UserRepository userRepository;

	@Override
	public RefreshToken createRefreshToken(String username) {

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found: " + username));

		RefreshToken refreshToken = user.getRefreshToken();

		if (Objects.isNull(refreshToken)) {
			refreshToken = RefreshToken.builder().user(user).tokenId(UUID.randomUUID().toString())
					.expiryDate(Instant.now().plusMillis(jwtRefreshExpirationMs)).build();
		} else {
			refreshToken.setExpiryDate(Instant.now().plusMillis(jwtRefreshExpirationMs));
		}

		user.setRefreshToken(refreshToken);

		return refreshTokenRepository.save(refreshToken);
	}

	@Override
	public Optional<RefreshToken> findByTokenId(String tokenId) {
		return refreshTokenRepository.findByTokenId(tokenId);
	}

	@Override
	public RefreshToken verifyExpiration(RefreshToken refreshToken) {
		if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(refreshToken);
			throw new RuntimeException(refreshToken.getTokenId() + " Refresh token is expired. Please make a new login..!");
		}
		return refreshToken;
	}

}
