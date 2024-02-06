package com.astarel.school.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.astarel.school.model.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

	Optional<Subject> findSubjectByTitle(String subject);

	Optional<Subject> findSubjectById(Long id);
}
