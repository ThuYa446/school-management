package com.astarel.school.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;

import com.astarel.school.auth.JwtUtil;
import com.astarel.school.exception.ApiErrorResponse;
import com.astarel.school.model.dto.AuthRequest;
import com.astarel.school.model.dto.AuthResponse;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

	@Mock
	private AuthenticationManager authManager;

	@Mock
	private JwtUtil jwtUtil;

	private AuthController controller;

	@BeforeEach
	void setUp() {
		controller = new AuthController();
		controller.authManager = authManager;
		controller.jwtUtil = jwtUtil;
	}

	@Test
	void csrfReturnsTheProvidedToken() {
		CsrfToken token = new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", "value");

		assertThat(controller.csrf(token)).isSameAs(token);
	}

	@Test
	void logInReturnsJwtWhenAuthenticationSucceeds() {
		AuthRequest request = AuthRequest.builder().email("446thuya446@gmail.com").password("12345").build();
		Authentication authentication = mock(Authentication.class);
		List<GrantedAuthority> authorities =
		        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
		when(authManager.authenticate(any())).thenReturn(authentication);
		when(authentication.isAuthenticated()).thenReturn(true);
		when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);
		when(jwtUtil.generateToken("446thuya446@gmail.com", List.of("ROLE_ADMIN"))).thenReturn("jwt-token");

		ResponseEntity<Object> response = controller.logIn(request);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(AuthResponse.builder().accessToken("jwt-token").build());
	}

	@Test
	void logInReturnsBadRequestWhenAuthenticationFails() {
		AuthRequest request = AuthRequest.builder().email("446thuya446@gmail.com").password("wrong").build();
		when(authManager.authenticate(any())).thenThrow(new BadCredentialsException("bad"));

		ResponseEntity<Object> response = controller.logIn(request);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isInstanceOf(ApiErrorResponse.class);
		assertThat(((ApiErrorResponse) response.getBody()).getErrorCode()).isEqualTo("400");
	}
}
