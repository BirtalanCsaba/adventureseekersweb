package com.adventureseekers.adventurewebapi.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.adventureseekers.adventurewebapi.dao.RoleDAO;
import com.adventureseekers.adventurewebapi.dao.UserDAO;
import com.adventureseekers.adventurewebapi.entity.RoleEntity;
import com.adventureseekers.adventurewebapi.entity.UserDetailEntity;
import com.adventureseekers.adventurewebapi.entity.UserEntity;
import com.adventureseekers.adventurewebapi.exception.UserAlreadyExistException;
import com.adventureseekers.adventurewebapi.exception.UserNotFoundException;
import com.adventureseekers.adventurewebapi.rest.UserRestController;

@Service
public class UserServiceImpl implements UserService {
	
	private Logger logger = LoggerFactory.getLogger(UserRestController.class);
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private RoleDAO roleDAO;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	@Transactional
	public Optional<UserEntity> findByUserName(String userName) {
		// check the database if the user already exists
		return this.userDAO.findByUsername(userName);
	}
	
	@Override
	@Transactional
	public void save(UserEntity newUser) throws UserAlreadyExistException {
		// check the uniqueness of the user 
		if (this.userDAO.existsByUsername(newUser.getUserName())) {
			throw new UserAlreadyExistException("A user already exists with username - " + newUser.getUserName());
		}
		if (this.userDAO.existsByEmail(newUser.getEmail())) {
			throw new UserAlreadyExistException("A user already exists with email - " + newUser.getEmail());
		}
		
		UserEntity user = new UserEntity();
		
		// assign user details to the user object
		user.setUserName(newUser.getUserName());
		user.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
		user.setFirstName(newUser.getFirstName());
		user.setLastName(newUser.getLastName());
		user.setEmail(newUser.getEmail());
		user.setBirthDate(newUser.getBirthDate());
		
		// give user default role of "employee"
		user.setRoles(Arrays.asList(this.roleDAO.findRoleByName("ROLE_STANDARD").get()));
		
		// create user details
		user.setUserDetail(new UserDetailEntity());
		
		// save the user in the database
		this.userDAO.save(user);
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = 
				this.userDAO.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));

		// check whether the user account is confirmed
		if (!user.isEnabled()) {
			throw new UsernameNotFoundException("The user account is not enabled");
		}
		return new User(user.getUserName(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}
	
	/**
	 * Converts the roles to a map of authorities
	 * @param collection The roles to be converted
	 * @return A map of authorities
	 */
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<RoleEntity> collection) {
		return collection.stream().map(role -> 
						new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	public Optional<UserEntity> findByEmail(String email) {
		return this.userDAO.findByEmail(email);
	}

	@Override
	public Boolean existsByEmail(String email) {
		return this.userDAO.existsByEmail(email);
	}

	@Override
	public Boolean existsByUsername(String username) {
		return this.userDAO.existsByUsername(username);
	}

	@Override
	public void update(UserEntity newUser) {
		// save the user in the database
		this.userDAO.save(newUser);
	}

	@Override
	public void delete(String username) throws UserNotFoundException {
		// TODO: AOP for resource protection
		//this.checkRequestPermission(username);
		UserEntity theUser = this.userDAO.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(username));
		this.userDAO.deleteById(theUser.getId());
	}
	
	/*private void checkRequestPermission(String username) throws IllegalStateException {
		// TODO: make an AOP which checks the permission
		// the currently logged in user's user name
		String authUsername =
				SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		if (!StringUtils.pathEquals(username, authUsername)) {
			throw new IllegalStateException("Access Denied");
		}
	}*/
}














