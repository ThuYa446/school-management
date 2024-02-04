package com.astarel.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.astarel.school.auth.JwtUtil;
import com.astarel.school.model.dto.AuthRequest;
import com.astarel.school.model.dto.AuthResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {
	
	 @Autowired
	 AuthenticationManager authManager;

	    @Autowired
	    JwtUtil jwtUtil;

	    @PostMapping("/auth/login")
	    public AuthResponse logIn(@RequestBody @Valid AuthRequest authRequest){
	    	Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
	        if(authentication.isAuthenticated()){
	           return AuthResponse.builder().
	        		   accessToken(jwtUtil.GenerateToken(authRequest.getEmail())).build();
	        } else {
	            throw new UsernameNotFoundException("invalid user request..!!");
	        }
	    }

}
