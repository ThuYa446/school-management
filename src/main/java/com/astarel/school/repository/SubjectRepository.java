package com.astarel.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.astarel.school.model.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject,Long >{

}
