package com.astarel.school.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.astarel.school.model.entity.ClassRoom;

public interface ClassRoomRepository  extends JpaRepository<ClassRoom,Long >{

	Optional<ClassRoom> findClassRoomByClassName(String className);
	
	Optional<ClassRoom> findClassRoomById(Long id);
}
