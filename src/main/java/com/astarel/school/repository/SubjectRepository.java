package com.astarel.school.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.astarel.school.model.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

	Optional<Subject> findSubjectByTitle(String subject);

	Optional<Subject> findSubjectById(Long id);
	
	List<Subject> findByTeacherIsNull();
	
	@Query("SELECT subj FROM Subject subj JOIN subj.student stu  WHERE stu.id = :id")
	Optional<Subject> getSubjectByStudentId(Long id);
}
