package com.adventureseekers.adventurewebapi.rest;

import static com.adventureseekers.adventurewebapi.rest.RestTestHelper.asJsonString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.adventureseekers.adventurewebapi.entity.RoleEntity;
import com.adventureseekers.adventurewebapi.entity.UserDetailEntity;
import com.adventureseekers.adventurewebapi.entity.UserEntity;
import com.adventureseekers.adventurewebapi.exception.UserNotFoundException;
import com.adventureseekers.adventurewebapi.service.PendingEmailService;
import com.adventureseekers.adventurewebapi.service.UserService;

@ExtendWith(MockitoExtension.class)
public class AuthenticationRestControllerTest {
	
	@Mock private PendingEmailService pendingEmailService;
	
	@Mock private UserService userService;
	
	@InjectMocks
	private AuthenticationRestController underTest;
	
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
	
	@Test
	public void canRegister() throws Exception {
		// when
		when(this.userService.findByUserName(this.userEntity.getUserName()))
			.thenReturn(Optional.of(this.userEntity));
		this.mockMvc.perform(
				post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(this.userEntity)))
				.andExpect(status().isCreated());
		
		// then
		ArgumentCaptor<UserEntity> userArgumentCaptor = 
				ArgumentCaptor.forClass(UserEntity.class);
		verify(this.userService).save(userArgumentCaptor.capture());
		UserEntity capturedUser = userArgumentCaptor.getValue();
		
		assertThat(capturedUser).isEqualTo(this.userEntity);
		
		ArgumentCaptor<String> pendingEmailArgumentCaptor =
				ArgumentCaptor.forClass(String.class);
		
		verify(this.pendingEmailService)
			.create(userArgumentCaptor.capture(), 
					pendingEmailArgumentCaptor.capture());
		
		capturedUser = userArgumentCaptor.getValue();
		String capturedPendingEmail = pendingEmailArgumentCaptor.getValue();
		
		assertThat(capturedUser).isEqualTo(this.userEntity);
		assertThat(capturedPendingEmail).isEqualTo(this.userEntity.getEmail());
	}
	
	@Test
	public void registerWillThrowWhenUserNotFound() throws Exception {
		// when
		when(this.userService.findByUserName(this.userEntity.getUserName()))
			.thenReturn(Optional.empty());
		this.mockMvc.perform(
				post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(this.userEntity)))
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() 
						instanceof UserNotFoundException));
	}
	
	@Test
	public void canConfirm() throws Exception {
		String token = UUID.randomUUID().toString();
		this.mockMvc.perform(
				get("/api/auth/confirmation")
				.param("token", token))
				.andExpect(status().isOk());
	}
	
	@Test
	public void canResend() throws Exception {
		this.mockMvc.perform(
				get("/api/auth/resend")
				.param("email", this.userEntity.getEmail()))
				.andExpect(status().isOk());
	}
	
}






















