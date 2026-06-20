package com.astarel.school.auth;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

class JwtUtilTest {

	private JwtUtil jwtUtil;

	@BeforeEach
	void setUp() {
		jwtUtil = new JwtUtil();
		ReflectionTestUtils.setField(jwtUtil, "objectMapper", new ObjectMapper());
	}

	@Test
	void generateTokenAllowsClaimsToBeExtracted() {
		String token = jwtUtil.generateToken("admin@school.com", List.of("ROLE_ADMIN", "ROLE_USER"));

		assertThat(jwtUtil.extractUsername(token)).isEqualTo("admin@school.com");
		assertThat(jwtUtil.extractRoles(token)).containsExactly("ROLE_ADMIN", "ROLE_USER");
		assertThat(jwtUtil.extractExpiration(token)).isAfter(new java.util.Date());
	}

	@Test
	void validateTokenMatchesTheExpectedUserOnly() {
		String token = jwtUtil.generateToken("admin@school.com", List.of("ROLE_ADMIN"));
		User matchingUser = new User("admin@school.com", "secret", List.of());
		User otherUser = new User("user@school.com", "secret", List.of());

		assertThat(jwtUtil.validateToken(token, matchingUser)).isTrue();
		assertThat(jwtUtil.validateToken(token, otherUser)).isFalse();
	}
}
