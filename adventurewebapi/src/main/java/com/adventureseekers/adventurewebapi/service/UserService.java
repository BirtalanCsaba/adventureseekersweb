package com.adventureseekers.adventurewebapi.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.adventureseekers.adventurewebapi.entity.UserEntity;
import com.adventureseekers.adventurewebapi.exception.UserNotFoundException;

public interface UserService extends UserDetailsService {
	
	/**
	 * Finds a use by the user name
	 * @param userName The user name of the user
	 * @return The use with the given user name
	 */
	Optional<UserEntity> findByUserName(String userName);
	
	/**
	 * Finds a use by the email
	 * @param email The email of the user
	 * @return The use with the given email
	 */
	Optional<UserEntity> findByEmail(String email);
	
	/**
	 * Check if there is a user with the given email
	 * @param email The email to be checked
	 * @return True if a user exists with the email, otherwise false
	 */
	Boolean existsByEmail(String email);
	
	/**
	 * Check if there is a user with the given user name
	 * @param username The user name of the user
	 * @return True if a user exists with the given user name, otherwise false
	 */
	Boolean existsByUsername(String username);
	
	/**
	 * Saves the given user to the database.
	 * @param adventureUser The user to be saved
	 * @exception If the user already exists. Email and user name must be unique.
	 */
	void save(UserEntity newUser);
	
	/**
	 * Updates the given user
	 * @param newUser The user to be updated
	 */
	void update(UserEntity newUser);
	
	/**
	 * Delete the given user with the given username
	 * @param username The username of the user
	 * @exception UserNotFoundException if the username is not valid
	 */
	void delete(String username) throws UserNotFoundException;
}










