package com.example.payload;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SignupRequest {
	
    private String name;
    private String username;
    private String email;
    private String password;
}