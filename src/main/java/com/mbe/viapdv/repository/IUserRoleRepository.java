package com.mbe.viapdv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mbe.viapdv.model.userRole.UserRole;
import com.mbe.viapdv.model.userRole.UserRoleId;

@Repository
public interface IUserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
	
	List<UserRole> findByActiveTrue();
	
	@Query("SELECT u FROM UserRole u WHERE u.id.user.id = :userId")
	List<UserRole> findByUserId(@Param("userId") long userId);


}
