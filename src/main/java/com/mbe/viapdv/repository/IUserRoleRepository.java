package com.mbe.viapdv.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mbe.viapdv.model.role.Role;
import com.mbe.viapdv.model.userRole.UserRole;
import com.mbe.viapdv.model.userRole.UserRoleId;

@Repository
public interface IUserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
	
	List<UserRole> findByActiveTrue();
	
	@Query("SELECT u FROM UserRole u WHERE u.id.user.id = :userId")
	List<UserRole> findByUserId(@Param("userId") long userId);
	
	@Query("SELECT ur FROM UserRole ur WHERE ur.id.user.id = :userId AND ur.id.role.id = :roleId")
	Optional<UserRole> findByUserIdAndRoleId(@Param("userId") long userId, @Param("roleId") long roleId);
	
	@Query("SELECT ur.id.role FROM UserRole ur WHERE ur.id.user.id = :userId AND ur.active = TRUE")
	List<Role> findRolesByUserId(@Param("userId") Long userId);

}
