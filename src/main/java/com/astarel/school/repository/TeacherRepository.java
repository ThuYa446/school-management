package com.astarel.school.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.astarel.school.model.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher,Long > {

	Optional<Teacher> findTeacherByEmail(String email);
	
	Optional<Teacher> findTeacherById(Long id);
}
