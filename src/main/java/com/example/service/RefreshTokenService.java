package com.example.service;

import java.util.Optional;

import com.example.model.RefreshToken;

public interface RefreshTokenService {

	RefreshToken createRefreshToken(String username);
	
	Optional<RefreshToken> findByTokenId(String tokenId);
	
	RefreshToken verifyExpiration(RefreshToken tokenId);

}
