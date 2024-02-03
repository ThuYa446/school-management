package com.astarel.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.astarel.school.model.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher,Long > {

}
