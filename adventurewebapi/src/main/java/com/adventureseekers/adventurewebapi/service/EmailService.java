package com.adventureseekers.adventurewebapi.service;

public interface EmailService {
	
	/**
	 * Sends an email to a specified address
	 * @param to Who is the receiver
	 * @param email The email of the receiver
	 * @param subject The subject of the email
	 */
	public void send(String to, String email, String subject);
}
