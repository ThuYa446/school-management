package com.astarel.school.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.astarel.school.model.entity.User;

public interface UserRepository extends JpaRepository<User,Long >{

	Optional<User> findByEmail(String email);
}
