package com.astarel.school.model.dto;

import org.springframework.security.core.userdetails.UserDetails;

import com.astarel.school.model.entity.User;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class CustomUserDetails extends User implements UserDetails {

	private static final long serialVersionUID = 1L;
	private String email;
    private String password;

    public CustomUserDetails(User byUsername) {
        this.email = byUsername.getEmail();
        this.password= byUsername.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
