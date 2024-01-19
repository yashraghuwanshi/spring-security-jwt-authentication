package com.example.service;

import java.util.List;

import com.example.model.User;
import com.example.payload.SignupRequest;

public interface UserService {

	void saveUser(SignupRequest signupRequest);
	List<User> getUser();

}
