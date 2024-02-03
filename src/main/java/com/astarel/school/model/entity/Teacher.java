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
@ToString(callSuper=true)
@Entity
@EqualsAndHashCode(callSuper=true)
public class Teacher  extends Human implements Serializable{
	private static final long serialVersionUID = 1L;

	@Column 
	private String phoneNo;
	
	@Column 
	private String address;
	
	@OneToMany(
			cascade= CascadeType.ALL,
			fetch = FetchType.LAZY ,
			mappedBy = "teacher"
			)
	private List<Subject> subject;
	
}
