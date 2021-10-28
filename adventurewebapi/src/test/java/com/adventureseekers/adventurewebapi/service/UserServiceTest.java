package com.adventureseekers.adventurewebapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.adventureseekers.adventurewebapi.dao.RoleDAO;
import com.adventureseekers.adventurewebapi.dao.UserDAO;
import com.adventureseekers.adventurewebapi.entity.RoleEntity;
import com.adventureseekers.adventurewebapi.entity.UserDetailEntity;
import com.adventureseekers.adventurewebapi.entity.UserEntity;
import com.adventureseekers.adventurewebapi.exception.UserAlreadyExistException;
import com.adventureseekers.adventurewebapi.exception.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock private UserDAO userDAO;

	@Mock private RoleDAO roleDAO;
	
	@Mock private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Autowired
	@InjectMocks 
	private UserServiceImpl underTest;
	
	private UserEntity userEntity;
	
	@BeforeEach
	public void setUp() {
		this.userEntity = new UserEntity(
        		"user.test", 
        		"test123", 
        		"user@test.com", 
        		"firstName", 
        		"lastName", 
        		new Date(), 
        		false, 
        		new UserDetailEntity(), 
        		Arrays.asList(new RoleEntity("ROLE_STANDARD")));
	}
	
	
	@AfterEach
	public void tearDown() {
		this.userEntity = null;
	}
	
	@Test
	public void canSaveUser() {
		this.userEntity.setPassword(new BCryptPasswordEncoder().encode(this.userEntity.getPassword()));
		RoleEntity standardRole = new RoleEntity("ROLE_STANDARD");
		
		// when
		when(this.roleDAO.findRoleByName(any())).thenReturn(Optional.of(standardRole));
		when(this.passwordEncoder.encode(any())).thenReturn(this.userEntity.getPassword());
		
		this.underTest.save(this.userEntity);
		
		// then
		ArgumentCaptor<UserEntity> userArgumentCaptor = 
				ArgumentCaptor.forClass(UserEntity.class);
		
		verify(this.userDAO).save(userArgumentCaptor.capture());
		
		UserEntity capturedUser = userArgumentCaptor.getValue();
		
		assertThat(capturedUser)
			.usingRecursiveComparison()
			.ignoringFields("roles")
			.isEqualTo(this.userEntity);
	}
	
	@Test
	public void saveUserWillThrowWhenUserNameIsTaken() {
		when(this.userDAO.existsByUsername(this.userEntity.getUserName()))
			.thenReturn(true);
		
		// when
		// then
		assertThatThrownBy(() -> this.underTest.save(this.userEntity))
			.isInstanceOf(UserAlreadyExistException.class)
			.hasMessageContaining("A user already exists with username - " + this.userEntity.getUserName());
	}
	
	@Test
	public void saveUserWillThrowWhenEmailIsTaken() {
		when(this.userDAO.existsByEmail(this.userEntity.getEmail()))
			.thenReturn(true);
		
		// when
		// then
		assertThatThrownBy(() -> this.underTest.save(this.userEntity))
			.isInstanceOf(UserAlreadyExistException.class)
			.hasMessageContaining("A user already exists with email - " + this.userEntity.getEmail());
	}
	
	@Test
	public void canFindByUserName() {
		this.underTest.findByUserName(this.userEntity.getUserName());
		
		// then
		ArgumentCaptor<String> userNameArgumentCaptor = 
				ArgumentCaptor.forClass(String.class);
		
		verify(this.userDAO).findByUsername(userNameArgumentCaptor.capture());
		
		String capturedUserName = userNameArgumentCaptor.getValue();
		
		assertThat(capturedUserName).isEqualTo(this.userEntity.getUserName());
	}
	
	@Test
	public void canFindByEmail() {
		this.underTest.findByEmail(this.userEntity.getEmail());
		
		// then
		ArgumentCaptor<String> emailArgumentCaptor = 
				ArgumentCaptor.forClass(String.class);
		
		verify(this.userDAO).findByEmail(emailArgumentCaptor.capture());
		
		String capturedEmail = emailArgumentCaptor.getValue();
		
		assertThat(capturedEmail).isEqualTo(this.userEntity.getEmail());
	}
	
	@Test
	public void canExistsByEmail() {
		this.underTest.existsByEmail(this.userEntity.getEmail());
		
		// then
		ArgumentCaptor<String> emailArgumentCaptor = 
				ArgumentCaptor.forClass(String.class);
		
		verify(this.userDAO).existsByEmail(emailArgumentCaptor.capture());
		
		String capturedEmail = emailArgumentCaptor.getValue();
		
		assertThat(capturedEmail).isEqualTo(this.userEntity.getEmail());
	}
	
	@Test
	public void canExistsByUserName() {
		this.underTest.existsByUsername(this.userEntity.getUserName());
		
		// then
		ArgumentCaptor<String> userNameArgumentCaptor = 
				ArgumentCaptor.forClass(String.class);
		
		verify(this.userDAO).existsByUsername(userNameArgumentCaptor.capture());
		
		String capturedUserName = userNameArgumentCaptor.getValue();
		
		assertThat(capturedUserName).isEqualTo(this.userEntity.getUserName());
	}
	
	@Test
	public void canUpdate() {
		this.underTest.update(this.userEntity);
		
		// then
		ArgumentCaptor<UserEntity> userArgumentCaptor = 
				ArgumentCaptor.forClass(UserEntity.class);
		
		verify(this.userDAO).save(userArgumentCaptor.capture());
		
		UserEntity capturedUser = userArgumentCaptor.getValue();
		
		assertThat(capturedUser).isEqualTo(this.userEntity);
	}
	
	@Test
	public void canDelete() {
		when(this.userDAO.findByUsername(this.userEntity.getUserName()))
			.thenReturn(Optional.of(this.userEntity));
		
		this.underTest.delete(this.userEntity.getUserName());
		
		// then
		ArgumentCaptor<UUID> userIDArgumentCaptor = 
				ArgumentCaptor.forClass(UUID.class);
		
		verify(this.userDAO).deleteById(userIDArgumentCaptor.capture());
		
		UUID capturedUserID = userIDArgumentCaptor.getValue();
		
		assertThat(capturedUserID).isEqualTo(this.userEntity.getId());
	}
	
	@Test
	public void deleteWillThrowWhenUserNotFound() {
		when(this.userDAO.findByUsername(this.userEntity.getUserName()))
			.thenReturn(Optional.empty());
		
		// when
		// then
		assertThatThrownBy(() -> this.underTest.delete(this.userEntity.getUserName()))
			.isInstanceOf(UserNotFoundException.class)
			.hasMessageContaining("User not found - " + this.userEntity.getUserName());
	}
	
	@Test
	public void canLoadByUsername() {
		this.userEntity.setEnabled(true);
		when(this.userDAO.findByUsername(this.userEntity.getUserName()))
			.thenReturn(Optional.of(this.userEntity));
		
		User theUser = new User(
				this.userEntity.getUserName(), 
				this.userEntity.getPassword(),
				Arrays.asList(new SimpleGrantedAuthority(
						this.userEntity.getRoles().iterator().next().getName())));
		
		assertThat(this.underTest.loadUserByUsername(this.userEntity.getUserName()))
			.isEqualTo(theUser);
	}
	
	@Test
	public void loadUserByUsernameWillThrowWhenUserNameNotFound() {
		when(this.userDAO.findByUsername(this.userEntity.getUserName()))
			.thenReturn(Optional.empty());
		
		// when
		// then
		assertThatThrownBy(() -> this.underTest.loadUserByUsername(this.userEntity.getUserName()))
			.isInstanceOf(UsernameNotFoundException.class)
			.hasMessageContaining("Invalid username or password");
	}
	
	@Test
	public void loadUserByUsernameWillThrowWhenUserAccountNotEnabled() {
		this.userEntity.setEnabled(false);
		when(this.userDAO.findByUsername(this.userEntity.getUserName()))
			.thenReturn(Optional.of(this.userEntity));
		
		// when
		// then
		assertThatThrownBy(() -> this.underTest.loadUserByUsername(this.userEntity.getUserName()))
			.isInstanceOf(UsernameNotFoundException.class)
			.hasMessageContaining("The user account is not enabled");
	}

}














