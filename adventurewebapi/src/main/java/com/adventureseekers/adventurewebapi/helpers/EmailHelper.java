package com.adventureseekers.adventurewebapi.helpers;

import com.adventureseekers.adventurewebapi.entity.UserEntity;

public interface EmailHelper {
	
	/**
	 * Sends a verification email to the given user containing a link with the token
	 * @param theUser The user to be delivered
	 * @param token The email verification token
	 */
	public void sendVerificationEmail(UserEntity theUser, String token);
	
	/**
	 * Sends verification email to the user which has the given email containing a link with the token
	 * @param email The email of the user
	 */
	//public void sendVerificationEmail(String email);
}
