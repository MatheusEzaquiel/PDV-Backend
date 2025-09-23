package com.mbe.viapdv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbe.viapdv.model.user.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

	Optional<User> findByName(String name);
	Optional<User> findByEmail(String email);
	Optional<User> findByEmailAndActiveTrue(String email);

	
	Optional<User> findByIdAndActiveTrue(Long id);
	
}