package com.astarel.school.model.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper=true)
@Entity
@EqualsAndHashCode(callSuper=true)
public class Student extends Human implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Column 
	private String phoneNo;
	
	@Column 
	private String address;
	
	@Column
	@Enumerated(EnumType.STRING)
	private StudentType studentType;
	
	@JsonIgnore
	@ToString.Exclude
	@ManyToOne(cascade= CascadeType.ALL)
	@JoinColumn(name="class_room_id")
	private ClassRoom classRoom;
	
	@JsonIgnore
	@ToString.Exclude
	@ManyToOne(
			cascade= CascadeType.ALL
			)
	@JoinColumn(name="subject_id")
	private Subject subject;
	
}
