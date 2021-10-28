package com.adventureseekers.adventurewebapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.adventureseekers.adventurewebapi.dao.UserDAO;
import com.adventureseekers.adventurewebapi.dao.UserDetailDAO;
import com.adventureseekers.adventurewebapi.entity.UserDetailEntity;
import com.adventureseekers.adventurewebapi.entity.UserEntity;
import com.adventureseekers.adventurewebapi.exception.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
public class UserDetailServiceTest {
	
	@Mock private UserDAO userDAO;
	
	@Mock private UserDetailDAO userDetailDAO;
	
	@Autowired
	@InjectMocks
	private UserDetailServiceImpl underTest;
	
	private UserDetailEntity userDetailEntity;
	
	@BeforeEach
	public void setUp() {
		this.userDetailEntity = new UserDetailEntity(
				"desc.test",
				"countrytest",
				"countytest",
				"citytest",
				null);
	}
	
	@AfterEach
	public void tearDown() {
		this.userDetailEntity = null;
	}
	
	@Test
	public void canUpdate() {
		this.underTest.update(this.userDetailEntity);
		
		// then
		ArgumentCaptor<UserDetailEntity> userDetailArgumentCaptor = 
				ArgumentCaptor.forClass(UserDetailEntity.class);
		
		verify(this.userDetailDAO).save(userDetailArgumentCaptor.capture());
		
		UserDetailEntity capturedUserDetail = userDetailArgumentCaptor.getValue();
		
		assertThat(capturedUserDetail).isEqualTo(this.userDetailEntity);
	}
	
	@Test
	public void canGetByUserName() {
		String userName = "user.test";
		UserEntity userEntity = new UserEntity();
		when(this.userDAO.findByUsername(userName)).thenReturn(Optional.of(userEntity));
		
		// when
		this.underTest.getByUsername(userName);
	}
	
	@Test
	public void getByUserNameWillThrowWhenUserNameNotValid() {
		String userName = "user.test";
		UserEntity userEntity = new UserEntity();
		when(this.userDAO.findByUsername(userName)).thenReturn(Optional.empty());
		
		// when
		// then
		assertThatThrownBy(() -> this.underTest.getByUsername(userName))
			.isInstanceOf(UserNotFoundException.class)
			.hasMessageContaining("User not found - " + userName);
	}
	
}
















