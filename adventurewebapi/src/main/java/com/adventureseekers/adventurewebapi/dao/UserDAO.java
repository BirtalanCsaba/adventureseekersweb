package com.adventureseekers.adventurewebapi.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.adventureseekers.adventurewebapi.entity.UserEntity;

public interface UserDAO
		extends JpaRepository<UserEntity, UUID> {
	
	/**
	 * Gets a user by its user name
	 * @param userName The user name of the user
	 * @return The user object with the given user name
	 */
	@Query("SELECT u FROM UserEntity u WHERE u.userName =:uName")
	public Optional<UserEntity> findByUsername(@Param("uName") String userName);
	
	/**
	 * Gets a user by the email
	 * @param email The user email
	 * @return The user with the given email
	 */
	@Query("SELECT u FROM UserEntity u WHERE u.email =:uEmail")
	public Optional<UserEntity> findByEmail(@Param("uEmail") String email);
	
	/**
	 * Check whether a user exists with the given user name 
	 * @param userName The user name of the user
	 * @return True if the user exists, otherwise false.
	 */
	@Query("select count(u)>0 from UserEntity u where u.userName=:uName")
	public boolean existsByUsername(@Param("uName") String userName);
	
	/**
	 * Check whether a user exists with the given email
	 * @param userName The email of the user
	 * @return True if the user exists, otherwise false.
	 */
	@Query("select count(u)>0 from UserEntity u where u.email=:uEmail")
	public boolean existsByEmail(@Param("uEmail") String email);
}










