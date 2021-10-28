package com.adventureseekers.adventurewebapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
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
import org.springframework.test.util.ReflectionTestUtils;

import com.adventureseekers.adventurewebapi.dao.ConfirmationTokenDAO;
import com.adventureseekers.adventurewebapi.dao.PendingEmailDAO;
import com.adventureseekers.adventurewebapi.dao.UserDAO;
import com.adventureseekers.adventurewebapi.entity.ConfirmationTokenEntity;
import com.adventureseekers.adventurewebapi.entity.PendingEmailEntity;
import com.adventureseekers.adventurewebapi.entity.RoleEntity;
import com.adventureseekers.adventurewebapi.entity.UserDetailEntity;
import com.adventureseekers.adventurewebapi.entity.UserEntity;
import com.adventureseekers.adventurewebapi.exception.PendingEmailAlreadyExistsException;
import com.adventureseekers.adventurewebapi.exception.PendingEmailNotFoundException;
import com.adventureseekers.adventurewebapi.exception.TokenAlreadyConfirmedException;
import com.adventureseekers.adventurewebapi.exception.TokenExpiredException;
import com.adventureseekers.adventurewebapi.exception.TokenNotFoundException;
import com.adventureseekers.adventurewebapi.exception.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
public class PendingEmailServiceTest {
	
	@Mock ConfirmationTokenDAO confirmationTokenDAO;
	
	@Mock UserDAO userDAO;
	
	@Mock PendingEmailDAO pendingEmailDAO;
	
	private Integer confirmationDays = 7;
	
	@Autowired
	@InjectMocks
	private PendingEmailServiceImpl underTest;
	
	private ConfirmationTokenEntity confirmationTokenEntity;
	
	private UserEntity userEntity;
	
	private String pendingEmail;
	
	private PendingEmailEntity pendingEmailEntity;
	
	@BeforeEach
	public void setUp() {
		this.confirmationTokenEntity = new ConfirmationTokenEntity(
				UUID.randomUUID().toString(),
				LocalDateTime.now(),
				LocalDateTime.now().plusDays(this.confirmationDays));
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
		this.pendingEmail = "pending.email@test.com";
		this.pendingEmailEntity = new PendingEmailEntity(this.pendingEmail);
		this.pendingEmailEntity.setUser(this.userEntity);
		this.pendingEmailEntity.setConfirmationToken(this.confirmationTokenEntity);
		
		ReflectionTestUtils.setField(this.underTest, "confirmationDays", 7);
	}
	
	@AfterEach
	public void tearDown() {
		this.confirmationTokenEntity = null;
		this.userEntity = null;
		this.pendingEmailEntity = null;
		this.pendingEmail = null;
	}
	
	@Test
	public void canCreate() {
		// when
		when(this.userDAO.existsById(this.userEntity.getId()))
			.thenReturn(true);
		
		when(this.confirmationTokenDAO.findByToken(any()))
			.thenReturn(Optional.of(this.confirmationTokenEntity));
		
		this.underTest.create(this.userEntity, this.pendingEmail);
		
		// then
		ArgumentCaptor<ConfirmationTokenEntity> confirmationTokenArgumentCaptor = 
				ArgumentCaptor.forClass(ConfirmationTokenEntity.class);
		
		verify(this.confirmationTokenDAO).save(confirmationTokenArgumentCaptor.capture());
		
		ConfirmationTokenEntity capuredConfirmationToken = confirmationTokenArgumentCaptor.getValue();
		
		assertThatNoException().isThrownBy(() -> UUID.fromString(capuredConfirmationToken.getToken()));
		
		// check creation date time
		assertTrue(capuredConfirmationToken.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(1)));
		assertTrue(capuredConfirmationToken.getCreatedAt().isBefore(LocalDateTime.now().plusMinutes(1)));
		
		// check expiration date time
		assertTrue(capuredConfirmationToken.getExpiredAt().isAfter(LocalDateTime.now().plusDays(this.confirmationDays - 1)));
		assertTrue(capuredConfirmationToken.getExpiredAt().isBefore(LocalDateTime.now().plusDays(this.confirmationDays + 1)));
		
		// test save the pending email
		ArgumentCaptor<PendingEmailEntity> pendingEmailArgumentCaptor = 
				ArgumentCaptor.forClass(PendingEmailEntity.class);
		
		verify(this.pendingEmailDAO).save(pendingEmailArgumentCaptor.capture());
		
		PendingEmailEntity capturedPendingEmail = pendingEmailArgumentCaptor.getValue();
		
		assertThat(capturedPendingEmail)
			.usingRecursiveComparison()
			.ignoringFields("token")
			.isEqualTo(this.pendingEmailEntity);
		
	}
	
	@Test
	public void createWillThrowWhenUserNotFound() {
		// when
		when(this.userDAO.existsById(this.userEntity.getId()))
			.thenReturn(false);
		
		// then
		assertThatThrownBy(() -> this.underTest.create(this.userEntity, this.pendingEmail))
			.isInstanceOf(UserNotFoundException.class)
			.hasMessageContaining("User not found - " + this.userEntity.getUserName());
	}
	
	@Test
	public void createWillThrowWhenTokenNotFound() {
		// when
		when(this.userDAO.existsById(this.userEntity.getId()))
			.thenReturn(true);
		when(this.confirmationTokenDAO.findByToken(any()))
			.thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> this.underTest.create(this.userEntity, this.pendingEmail))
			.isInstanceOf(TokenNotFoundException.class);
	}
	
	@Test
	public void canConfirmEmail() {
		String token = UUID.randomUUID().toString();
		
		// when
		when(this.confirmationTokenDAO.findByToken(token))
			.thenReturn(Optional.of(this.confirmationTokenEntity));
		
		this.confirmationTokenEntity.setPendingEmail(this.pendingEmailEntity);
		this.pendingEmailEntity.setConfirmationToken(null);
		
		this.underTest.confirmEmail(token);
		
		// then
		ArgumentCaptor<ConfirmationTokenEntity> confirmationTokenArgumentCaptor = 
				ArgumentCaptor.forClass(ConfirmationTokenEntity.class);
		
		verify(this.confirmationTokenDAO).save(confirmationTokenArgumentCaptor.capture());
		
		ConfirmationTokenEntity capturedConfirmationToken = confirmationTokenArgumentCaptor.getValue();
		
		assertThat(capturedConfirmationToken)
			.usingRecursiveComparison()
			.ignoringFields("confirmedAt", "pendingEmail")
			.isEqualTo(this.confirmationTokenEntity);
		
		ArgumentCaptor<UserEntity> userArgumentCaptor = 
				ArgumentCaptor.forClass(UserEntity.class);
		
		verify(this.userDAO).save(userArgumentCaptor.capture());
		
		UserEntity capturedUser = userArgumentCaptor.getValue();
		
		assertThat(capturedUser).isEqualTo(this.userEntity);
	}
	
	@Test
	public void confirmEmailWillThrowWhenTokenNotFound() {
		String token = UUID.randomUUID().toString();
		
		// when
		when(this.confirmationTokenDAO.findByToken(token))
			.thenReturn(Optional.empty());
		
		// when
		// then
		assertThatThrownBy(() -> this.underTest.confirmEmail(token))
			.isInstanceOf(TokenNotFoundException.class);
	}
	
	@Test
	public void confirmEmailWillThrowWhenTokenAlreadyConfirmed() {
		String token = UUID.randomUUID().toString();
		this.confirmationTokenEntity.setConfirmedAt(LocalDateTime.now());
		// when
		when(this.confirmationTokenDAO.findByToken(token))
			.thenReturn(Optional.of(this.confirmationTokenEntity));
		
		// then
		assertThatThrownBy(() -> this.underTest.confirmEmail(token))
			.isInstanceOf(TokenAlreadyConfirmedException.class);
	}
	
	@Test
	public void confirmEmailWillThrowWhenTokenExpired() {
		String token = UUID.randomUUID().toString();
		this.confirmationTokenEntity.setConfirmedAt(null);
		
		this.confirmationTokenEntity.setExpiredAt(LocalDateTime.now().minusMinutes(1));
		// when
		when(this.confirmationTokenDAO.findByToken(token))
			.thenReturn(Optional.of(this.confirmationTokenEntity));
		
		// then
		assertThatThrownBy(() -> this.underTest.confirmEmail(token))
			.isInstanceOf(TokenExpiredException.class);
	}
	
	@Test
	public void confirmEmailWillThrowWhenPendingEmailNotFound() {
		String token = UUID.randomUUID().toString();
		this.confirmationTokenEntity.setConfirmedAt(null);
		this.confirmationTokenEntity.setExpiredAt(LocalDateTime.now().plusHours(1));
		
		this.confirmationTokenEntity.setPendingEmail(null);
		// when
		when(this.confirmationTokenDAO.findByToken(token))
			.thenReturn(Optional.of(this.confirmationTokenEntity));
		
		// then
		assertThatThrownBy(() -> this.underTest.confirmEmail(token))
			.isInstanceOf(PendingEmailNotFoundException.class);
	}
	
	@Test
	public void canResetTokenEmail() {
		this.pendingEmailEntity.getConfirmationToken().setConfirmedAt(null);
		this.pendingEmailEntity.getConfirmationToken().setExpiredAt(LocalDateTime.now().minusMinutes(10));
		when(this.pendingEmailDAO.findByEmail(this.pendingEmail))
			.thenReturn(Optional.of(this.pendingEmailEntity));
		
		this.underTest.resetTokenEmail(this.pendingEmail);
		
		// then
		ArgumentCaptor<ConfirmationTokenEntity> confirmationTokenArgumentCaptor = 
				ArgumentCaptor.forClass(ConfirmationTokenEntity.class);
		
		verify(this.confirmationTokenDAO).save(confirmationTokenArgumentCaptor.capture());
		
		assertTrue(confirmationTokenArgumentCaptor.getValue() != null);
	}
	
	@Test
	public void resetTokenEmailWillThrowWhenPendingEmailNotFound() {
		this.pendingEmailEntity.getConfirmationToken().setConfirmedAt(null);
		this.pendingEmailEntity.getConfirmationToken().setExpiredAt(LocalDateTime.now().plusHours(10));
		
		when(this.pendingEmailDAO.findByEmail(this.pendingEmail))
			.thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> this.underTest.resetTokenEmail(pendingEmail))
			.isInstanceOf(PendingEmailNotFoundException.class);
	}
	
	@Test
	public void resetTokenEmailWillThrowWhenTokenAlreadyConfirmed() {
		this.pendingEmailEntity.getConfirmationToken().setConfirmedAt(LocalDateTime.now());
		this.pendingEmailEntity.getConfirmationToken().setExpiredAt(LocalDateTime.now().minusHours(10));
		when(this.pendingEmailDAO.findByEmail(this.pendingEmail))
			.thenReturn(Optional.of(this.pendingEmailEntity));
		
		assertThatThrownBy(() -> this.underTest.resetTokenEmail(pendingEmail))
			.isInstanceOf(TokenAlreadyConfirmedException.class);
	}
	
	@Test
	public void resetTokenWillThrowWhenTokenNotExpired() {
		this.pendingEmailEntity.getConfirmationToken().setConfirmedAt(null);
		this.pendingEmailEntity.getConfirmationToken().setExpiredAt(LocalDateTime.now().plusHours(10));
		when(this.pendingEmailDAO.findByEmail(this.pendingEmail))
			.thenReturn(Optional.of(this.pendingEmailEntity));
		
		assertThatThrownBy(() -> this.underTest.resetTokenEmail(this.pendingEmail))
			.isInstanceOf(TokenExpiredException.class)
			.hasMessage("Token expired - " + "Token is not expired");
	}
	
	@Test
	public void createWillThrowWhenAlreadyIsPandingEmail() {
		when(this.userDAO.existsById(this.userEntity.getId()))
			.thenReturn(true);
		when(this.pendingEmailDAO.findPendingEmail(this.userEntity))
			.thenReturn(Optional.of(this.pendingEmailEntity));
		
		assertThatThrownBy(() -> this.underTest.create(this.userEntity, this.pendingEmail))
			.isInstanceOf(PendingEmailAlreadyExistsException.class)
			.hasMessage("There is already a pending email");
	}
	
}



















