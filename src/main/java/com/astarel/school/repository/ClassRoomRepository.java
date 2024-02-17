package com.astarel.school.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.astarel.school.model.entity.ClassRoom;

public interface ClassRoomRepository  extends JpaRepository<ClassRoom,Long >{

	Optional<ClassRoom> findClassRoomByClassName(String className);
	
	Optional<ClassRoom> findClassRoomById(Long id);
	
	@Query("SELECT COUNT(stu) FROM ClassRoom class JOIN class.student stu  WHERE class.id = :id")
	Integer getTotalNumberOfStudentByClassId(@Param("id") Long id);
	
	@Query("SELECT room FROM ClassRoom room JOIN room.student stu  WHERE stu.id = :id")
	Optional<ClassRoom> getClassRoomByStudentId(Long id);
}
