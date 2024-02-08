package com.astarel.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.astarel.school.auth.JwtUtil;
import com.astarel.school.exception.ApiErrorResponse;
import com.astarel.school.model.dto.AuthRequest;
import com.astarel.school.model.dto.AuthResponse;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api")
public class AuthController {

	@Autowired
	AuthenticationManager authManager;

	@Autowired
	JwtUtil jwtUtil;

	@PostMapping("/auth/login")
	public ResponseEntity<Object> logIn(@RequestBody @Valid AuthRequest authRequest) {

		try {
			Authentication authentication = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
			if (authentication.isAuthenticated()) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(AuthResponse.builder().accessToken(jwtUtil.GenerateToken(authRequest.getEmail())).build());
			}
		}catch(AuthenticationException exception) {
			log.info("Authentication Problem Occurs");
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ApiErrorResponse("400", "Invalid Authentication."));
	}

}
