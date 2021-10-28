package com.adventureseekers.adventurewebapi.helpers;

import java.util.Optional;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.adventureseekers.adventurewebapi.entity.UserEntity;
import com.adventureseekers.adventurewebapi.rest.UserRestController;
import com.adventureseekers.adventurewebapi.service.EmailService;
import com.adventureseekers.adventurewebapi.service.UserService;

@Component
public class EmailHelperImpl implements EmailHelper {
	
	@Autowired
	private SpringTemplateEngine thymeleafTemplateEngine;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserService userService;
	
	private Logger logger = LoggerFactory.getLogger(UserRestController.class);
	
	public EmailHelperImpl() {
		
	}

	@Override
	public void sendVerificationEmail(UserEntity theUser, String token) {
		// send the confirmation token via email
		try {
			this.emailService.send(
					theUser.getEmail(), 
					this.createConfirmationEmail(
							theUser.getFirstName() + " " + theUser.getLastName(), token),
					"Confirm your email");
		} catch (MessagingException e) {
			this.logger.error(e.toString());
		}
	}

	/*@Override
	public void sendVerificationEmail(String email) {
		Optional<UserEntity> theUser = this.userService.findByEmail(email);
		String token = theUser.get().getConfirmationTokens().get(0).getToken();
		// send the confirmation token via email
		try {
			this.emailService.send(
					theUser.get().getEmail(), 
					this.createConfirmationEmail(
							theUser.get().getFirstName() + " " + theUser.get().getLastName(), token),
					"Confirm your email");
		} catch (MessagingException e) {
			this.logger.error(e.toString());
		}
	}*/
	
	/**
	 * Creates an email confirmation HTML page
	 * @param name The name of the user
	 * @param token The confirmation token
	 * @return The HTML body as a string
	 */
	private String createConfirmationEmail(String name, String token) throws MessagingException {
		Context thymeleafContext = new Context();
		thymeleafContext.setVariable("name", name);
		thymeleafContext.setVariable("confirmationLink", 
				"http://localhost:4200" + "/confirm/" + token );
		String htmlBody = 
				thymeleafTemplateEngine.process("email/email-confirmation.html", thymeleafContext);
		return htmlBody;
	}

}
