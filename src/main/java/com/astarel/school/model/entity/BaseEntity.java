package com.astarel.school.model.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@MappedSuperclass
public class BaseEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@CreationTimestamp
	@Column
	private Date createdAt;
	
	@UpdateTimestamp
	@Column
	private Date updatedAt;
	
	public Boolean isEqualId(Long id) {
		return this.id==id;
	}
	
}
