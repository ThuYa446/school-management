package com.astarel.school.model.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@Entity
@EqualsAndHashCode(callSuper=true)
public class ClassRoom  extends BaseEntity implements Serializable{
private static final long serialVersionUID = 1L;
	
	@Column(unique = true)
	private String className;
	
	@OneToMany(
			cascade= CascadeType.ALL,
			fetch = FetchType.LAZY ,
			mappedBy = "classRoom"
			)
	private List<Student> student;
	
}
