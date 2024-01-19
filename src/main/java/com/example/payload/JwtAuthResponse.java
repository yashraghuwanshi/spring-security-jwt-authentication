package com.example.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class JwtAuthResponse {
  
	 private String accessToken;
	 private String tokenType = "Bearer";
	 private String tokenId;
	 

}