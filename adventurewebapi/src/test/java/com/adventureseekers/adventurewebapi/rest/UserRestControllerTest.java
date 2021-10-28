package com.adventureseekers.adventurewebapi.rest;

import java.util.Arrays;
import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.adventureseekers.adventurewebapi.entity.RoleEntity;
import com.adventureseekers.adventurewebapi.entity.UserDetailEntity;
import com.adventureseekers.adventurewebapi.entity.UserEntity;
import com.adventureseekers.adventurewebapi.model.assembler.UserDetailModelAssembler;
import com.adventureseekers.adventurewebapi.model.assembler.UserModelAssembler;
import com.adventureseekers.adventurewebapi.service.PendingEmailService;
import com.adventureseekers.adventurewebapi.service.UserDetailService;
import com.adventureseekers.adventurewebapi.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserRestControllerTest {
	
	@Mock private PendingEmailService pendingEmailService;
	
	@Mock private UserService userService;
	
	@Mock private UserDetailService userDetailService;
	
	@Mock private UserModelAssembler userModelAssembler;
	
	@Mock private UserDetailModelAssembler userDetailModelAssembler;
	
	@InjectMocks
	private UserRestController underTest;
	
	private UserEntity userEntity;
	
	private UserDetailEntity userDetailEntity;
	
	@Autowired
	private MockMvc mockMvc;
	
	@BeforeEach
	public void setUp() {
		this.userDetailEntity = new UserDetailEntity(
				"desc.test",
				"countrytest",
				"countytest",
				"citytest",
				null);
		this.userEntity = new UserEntity(
        		"user.test", 
        		"test123", 
        		"user@test.com", 
        		"firstName", 
        		"lastName", 
        		new Date(), 
        		false, 
        		this.userDetailEntity, 
        		Arrays.asList(new RoleEntity("ROLE_STANDARD")));
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.underTest)
				.setControllerAdvice(
						new UserRestExceptionHandler(),
						new GlobalRestExceptionHandler()
						)
				.build();
	}
	
	@AfterEach
	public void tearDown() {
		this.userEntity = null;
	}

}














