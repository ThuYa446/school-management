package com.astarel.school.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
	
	@NotBlank(message = "{required.user.email}")
	@NotNull(message = "{required.user.email}")
	private String email;

	private String password;

}
