package com.adventureseekers.adventurewebapi.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adventureseekers.adventurewebapi.dao.ConfirmationTokenDAO;
import com.adventureseekers.adventurewebapi.dao.PendingEmailDAO;
import com.adventureseekers.adventurewebapi.dao.UserDAO;
import com.adventureseekers.adventurewebapi.entity.ConfirmationTokenEntity;
import com.adventureseekers.adventurewebapi.entity.PendingEmailEntity;
import com.adventureseekers.adventurewebapi.entity.UserEntity;
import com.adventureseekers.adventurewebapi.exception.PendingEmailAlreadyExistsException;
import com.adventureseekers.adventurewebapi.exception.PendingEmailNotFoundException;
import com.adventureseekers.adventurewebapi.exception.TokenAlreadyConfirmedException;
import com.adventureseekers.adventurewebapi.exception.TokenExpiredException;
import com.adventureseekers.adventurewebapi.exception.TokenNotFoundException;
import com.adventureseekers.adventurewebapi.exception.UserNotFoundException;

@Service
public class PendingEmailServiceImpl
		implements PendingEmailService {
	
	@Autowired
	private PendingEmailDAO pendingEmailDAO;
	
	@Autowired
	private ConfirmationTokenDAO confirmationTokenDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	/*@Autowired
	private EmailHelper emailHelper;*/
	
	@Value("${user.email.confirmation.expiration.days}")
	private Integer confirmationDays;
	
	@Override
	public void create(UserEntity theUser, String pendingEmail) 
			throws UserNotFoundException, TokenNotFoundException,
				PendingEmailAlreadyExistsException {
		// check if the user is valid
		if (!this.userDAO.existsById(theUser.getId()))
			throw new UserNotFoundException(theUser.getUserName());
		
		Optional<PendingEmailEntity> pendingEmailEntityOpt = 
				this.pendingEmailDAO.findPendingEmail(theUser);
		if (pendingEmailEntityOpt.isPresent()) {
			throw new PendingEmailAlreadyExistsException("There is already a pending email");
		}
		
		// create the token
		String token = UUID.randomUUID().toString();
		
		// create the confirmation token
		ConfirmationTokenEntity confirmationTokenEntity = 
				new ConfirmationTokenEntity(
						token,
		        		LocalDateTime.now(),
		        		LocalDateTime.now().plusDays(this.confirmationDays)
		        		);
		
		// save the token
		this.confirmationTokenDAO.save(confirmationTokenEntity);
		
		// get the new confirmation token
		confirmationTokenEntity = this.confirmationTokenDAO.findByToken(token)
					.orElseThrow(() -> new TokenNotFoundException(token));
		
		// create the pending email
		PendingEmailEntity pendingEmailEntity = 
				new PendingEmailEntity(pendingEmail);
		pendingEmailEntity.setConfirmationToken(confirmationTokenEntity);
		pendingEmailEntity.setUser(theUser);
		
		// save the pending email
		this.pendingEmailDAO.save(pendingEmailEntity);
		
		// TODO: send the confirmationEmail
	}

	@Override
	public void confirmEmail(String token)
			throws TokenAlreadyConfirmedException, TokenNotFoundException, 
				TokenExpiredException, PendingEmailNotFoundException {
		ConfirmationTokenEntity confirmationTokenEntity = 
				this.confirmationTokenDAO.findByToken(token)
				.orElseThrow(() -> new TokenNotFoundException(token));
		
		// check if token is already confirmed
		if (confirmationTokenEntity.getConfirmedAt() != null) {
			throw new TokenAlreadyConfirmedException(token);
		}
		
		// check if the token is not expired
		if (confirmationTokenEntity.getExpiredAt().isBefore(LocalDateTime.now())) {
			throw new TokenExpiredException(token);
		}
		
		// confirm the token
		confirmationTokenEntity.setConfirmedAt(LocalDateTime.now());
		this.confirmationTokenDAO.save(confirmationTokenEntity);
		
		// replace the user's email with the pending email
		PendingEmailEntity pendingEmailEntity = confirmationTokenEntity.getPendingEmail();
		if (pendingEmailEntity == null) {
			throw new PendingEmailNotFoundException("Pending email not found");
		}
		
		String pendingEmail = pendingEmailEntity.getEmail();
		UserEntity theUser = pendingEmailEntity.getUser();
		
		// change the email
		theUser.setEmail(pendingEmail);
		
		// enable the user
		theUser.setEnabled(true);
		
		// save the user
		this.userDAO.save(theUser);
	}

	@Override
	public void resetTokenEmail(String email) {
		PendingEmailEntity pendingEmail = 
				this.pendingEmailDAO.findByEmail(email)
				.orElseThrow(() -> new PendingEmailNotFoundException("Pending email not found"));
		
		// get the token
		ConfirmationTokenEntity confirmationToken = pendingEmail.getConfirmationToken();
		
		// check if token already confirmed
		if (confirmationToken.getConfirmedAt() != null) {
			throw new TokenAlreadyConfirmedException(confirmationToken.getToken());
		}
		
		if (confirmationToken.getExpiredAt().isAfter(LocalDateTime.now())) {
			throw new TokenExpiredException("Token is not expired");
		}
		
		// reset the token
		confirmationToken.setCreatedAt(LocalDateTime.now());
		confirmationToken.setExpiredAt(LocalDateTime.now().plusDays(this.confirmationDays));
		confirmationToken.setToken(UUID.randomUUID().toString());
		
		// update the token
		this.confirmationTokenDAO.save(confirmationToken);
	}
	
	
}










