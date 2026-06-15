package com.astarel.school.model.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString(exclude = "roles")
@EqualsAndHashCode(callSuper = true, exclude = "roles")
@AllArgsConstructor
@NoArgsConstructor
public class User extends Human implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Column
	private String password;

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}
	
	@OneToMany(
			cascade = CascadeType.ALL,
			orphanRemoval = true,
			fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private Set<Role> roles = new HashSet<Role>();

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

}
