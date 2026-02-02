package com.astarel.school.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
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

	@GetMapping("/csrf-token")
	public CsrfToken csrf(CsrfToken token) {
		return token; // This ensures token is created and sent as cookie
	}

	@PostMapping("/auth/login")
	public ResponseEntity<Object> logIn(@RequestBody @Valid AuthRequest authRequest) {

		try {
			Authentication authentication = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
			if (authentication.isAuthenticated()) {
				List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
						.collect(Collectors.toList());
				log.info("Roles - " + roles);
				return ResponseEntity.status(HttpStatus.OK).body(AuthResponse.builder()
						.accessToken(jwtUtil.generateToken(authRequest.getEmail(), roles)).build());
			}
		} catch (AuthenticationException exception) {
			log.info("Authentication Problem Occurs");
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ApiErrorResponse("400", "Invalid Credential.\nPlease check username and password."));
	}

}
