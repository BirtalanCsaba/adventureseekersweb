package com.adventureseekers.adventurewebapi.service;

import com.adventureseekers.adventurewebapi.entity.UserEntity;
import com.adventureseekers.adventurewebapi.exception.PendingEmailNotFoundException;
import com.adventureseekers.adventurewebapi.exception.TokenAlreadyConfirmedException;
import com.adventureseekers.adventurewebapi.exception.TokenExpiredException;
import com.adventureseekers.adventurewebapi.exception.TokenNotFoundException;
import com.adventureseekers.adventurewebapi.exception.UserNotFoundException;

public interface PendingEmailService {
	
	public void create(UserEntity theUser, String pendingEmail)
		throws UserNotFoundException, TokenNotFoundException;
	
	public void confirmEmail(String token)
		throws TokenAlreadyConfirmedException, TokenNotFoundException, 
			TokenExpiredException, PendingEmailNotFoundException;
	
	public void resetTokenEmail(String pendingEmail);
	
}
