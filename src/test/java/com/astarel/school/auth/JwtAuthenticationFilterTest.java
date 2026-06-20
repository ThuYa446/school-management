package com.astarel.school.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.astarel.school.service.impl.UserDetailsServiceImpl;
import com.astarel.school.auth.JwtAuthenticationFilter;
import jakarta.servlet.ServletException;

@SuppressWarnings("unused")
@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

	@Mock
	private JwtUtil jwtUtil;

	@Mock
	private UserDetailsServiceImpl userDetailsServiceImpl;

	private JwtAuthenticationFilter filter;

	@BeforeEach
	void setUp() {
		filter = new JwtAuthenticationFilter();
		filter.jwtUtil = jwtUtil;
		filter.userDetailsServiceImpl = userDetailsServiceImpl;
	}

	@AfterEach
	void tearDown() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void doFilterInternalSkipsAuthenticationWhenAuthorizationHeaderIsMissing() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		filter.doFilterInternal(request, response, new MockFilterChain());

		assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
		verify(userDetailsServiceImpl, never()).loadUserByUsername(org.mockito.ArgumentMatchers.anyString());
	}

	@Test
	void doFilterInternalAuthenticatesWhenBearerTokenIsValid() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Authorization", "Bearer test-token");
		MockHttpServletResponse response = new MockHttpServletResponse();
		User user = new User("admin@school.com", "secret",
				List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

		when(jwtUtil.extractUsername("test-token")).thenReturn("admin@school.com");
		when(userDetailsServiceImpl.loadUserByUsername("admin@school.com")).thenReturn(user);
		when(jwtUtil.validateToken("test-token", user)).thenReturn(true);

		filter.doFilterInternal(request, response, new MockFilterChain());

		assertThat(SecurityContextHolder.getContext().getAuthentication())
				.isInstanceOf(UsernamePasswordAuthenticationToken.class);
		assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo("admin@school.com");
	}

	@Test
	void doFilterInternalDoesNotOverwriteExistingAuthentication() throws ServletException, IOException {
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken("existing", null, List.of()));
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Authorization", "Bearer test-token");
		MockHttpServletResponse response = new MockHttpServletResponse();

		when(jwtUtil.extractUsername("test-token")).thenReturn("admin@school.com");

		filter.doFilterInternal(request, response, new MockFilterChain());

		assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo("existing");
		verify(userDetailsServiceImpl, never()).loadUserByUsername("admin@school.com");
	}
}
