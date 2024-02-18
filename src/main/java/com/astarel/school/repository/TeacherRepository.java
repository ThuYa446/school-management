package com.astarel.school.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.astarel.school.model.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher,Long > {

	Optional<Teacher> findTeacherByEmail(String email);
	
	Optional<Teacher> findTeacherById(Long id);
	
	@Query("SELECT teach FROM Teacher teach JOIN teach.subject subj  WHERE subj.id = :id")
	Optional<Teacher> getTeacherBySubjectId(Long id);
}
