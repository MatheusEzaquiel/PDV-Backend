package com.mbe.viapdv.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbe.viapdv.model.role.Role;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
	
	List<Role> findByActiveTrue();
	
	Optional<Role> findByIdAndActiveTrue(Long id);
}
