package com.adventureseekers.adventurewebapi.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.adventureseekers.adventurewebapi.entity.RoleEntity;
import com.adventureseekers.adventurewebapi.entity.UserEntity;

public interface RoleDAO
		extends JpaRepository<UserEntity, Long>{
	
	/**
	 * Gets the role by name
	 * @param theRoleName The name of the role
	 * @return The role object with the given name
	 */
	@Query("SELECT r FROM RoleEntity r WHERE r.name =:rName")
	public Optional<RoleEntity> findRoleByName(@Param("rName")String theRoleName);
}
