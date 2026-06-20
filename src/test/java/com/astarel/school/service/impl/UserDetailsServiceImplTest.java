package com.astarel.school.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import com.astarel.school.TestDataFactory;
import com.astarel.school.model.entity.User;
import com.astarel.school.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

	@Mock
	private UserRepository userRepository;

	private UserDetailsServiceImpl service;

	@BeforeEach
	void setUp() {
		service = new UserDetailsServiceImpl();
		ReflectionTestUtils.setField(service, "userRepository", userRepository);
	}

	@Test
	void loadUserByUsernameBuildsSpringSecurityUserFromEntityRoles() {
		User user = TestDataFactory.userEntity("admin@school.com", "encoded", "ADMIN", "USER");
		when(userRepository.findByEmail("admin@school.com")).thenReturn(Optional.of(user));

		org.springframework.security.core.userdetails.UserDetails result = service
				.loadUserByUsername("admin@school.com");

		assertThat(result.getUsername()).isEqualTo("admin@school.com");
		assertThat(result.getPassword()).isEqualTo("encoded");
		assertThat(result.getAuthorities()).extracting("authority").containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_USER");
	}

	@Test
	void loadUserByUsernameThrowsWhenEmailDoesNotExist() {
		when(userRepository.findByEmail("missing@school.com")).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.loadUserByUsername("missing@school.com"))
				.isInstanceOf(UsernameNotFoundException.class)
				.hasMessageContaining("could not found email");
	}
}
