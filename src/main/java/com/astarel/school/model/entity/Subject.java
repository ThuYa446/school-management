package com.astarel.school.model.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@Entity
@EqualsAndHashCode(callSuper=true)
public class Subject  extends BaseEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Column 
	private String title;
	
	@JsonIgnore
	@ToString.Exclude
	@ManyToOne(
			cascade= CascadeType.ALL
			)
	@JoinColumn(name="teacher_id")
	private Teacher teacher;
	
	@OneToMany(
			cascade= CascadeType.ALL,
			fetch = FetchType.LAZY ,
			mappedBy = "subject"
			)
	private List<Student> student;
}
