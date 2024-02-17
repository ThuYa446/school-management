package com.astarel.school.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.astarel.school.model.entity.Student;

public interface StudentRepository  extends JpaRepository<Student, Long>{

	Optional<Student> findStudentByEmail(String email);
	
	Optional<Student> findStudentById(Long id);
	
	List<Student> findBySubjectIsNull();
	
	List<Student> findByClassRoomIsNull();
	
	@Query("SELECT stu FROM Subject subj JOIN subj.student stu  WHERE subj.id = :id")
	List<Student> getStudentsBySubjectId(Long id);
}
