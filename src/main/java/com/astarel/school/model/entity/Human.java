package com.astarel.school.model.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@MappedSuperclass
@EqualsAndHashCode(callSuper=true)
public class Human extends BaseEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Column
	private String name;
	
	@Column(unique = true)
	private String email;
}
