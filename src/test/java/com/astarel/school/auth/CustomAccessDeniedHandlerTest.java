package com.astarel.school.auth;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import com.astarel.school.auth.CustomAccessDeniedHandler;
@SuppressWarnings("unused")
class CustomAccessDeniedHandlerTest {

	private final CustomAccessDeniedHandler handler = new CustomAccessDeniedHandler();

	@Test
	void handleWritesForbiddenJsonResponse() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/students");
		MockHttpServletResponse response = new MockHttpServletResponse();

		handler.handle(request, response, new AccessDeniedException("denied"));

		assertThat(response.getStatus()).isEqualTo(403);
		assertThat(response.getContentType()).isEqualTo("application/json");
		assertThat(response.getContentAsString()).contains("\"error\": \"Forbidden\"");
		assertThat(response.getContentAsString()).contains("\"path\": \"/api/students\"");
	}
}
