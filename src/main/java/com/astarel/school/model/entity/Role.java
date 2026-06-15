package com.astarel.school.model.entity;

import org.apache.commons.lang3.builder.ToStringExclude;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@Entity
@EqualsAndHashCode(callSuper=true)
public class Role extends BaseEntity implements GrantedAuthority{

	private static final long serialVersionUID = 1L;

	@Column(nullable = false, unique = true)
	private String role;
	
	@JsonIgnore
	@ToStringExclude
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;

	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
