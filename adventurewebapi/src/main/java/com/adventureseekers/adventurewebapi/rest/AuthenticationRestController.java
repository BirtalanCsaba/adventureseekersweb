package com.adventureseekers.adventurewebapi.rest;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adventureseekers.adventurewebapi.entity.UserEntity;
import com.adventureseekers.adventurewebapi.exception.UserNotFoundException;
import com.adventureseekers.adventurewebapi.response.StringResponse;
import com.adventureseekers.adventurewebapi.service.PendingEmailService;
import com.adventureseekers.adventurewebapi.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationRestController {
	
	private Logger logger = LoggerFactory.getLogger(UserRestController.class);
	
	@Autowired
	private PendingEmailService pendingEmailService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * Registers a new user
	 * @param user The user to be registered
	 */
	@PostMapping("/register")
	private ResponseEntity<StringResponse> register(
			@Valid @RequestBody UserEntity user) {
		// register the user
		this.userService.save(user);
		
		// retrieve the new user
        UserEntity theUser = this.userService.findByUserName(user.getUserName())
        		.orElseThrow(() -> new UserNotFoundException(user.getUserName()));
        
		this.pendingEmailService.create(theUser, theUser.getEmail());
		
		return new ResponseEntity<StringResponse>(
				new StringResponse("Account created successfuly"), HttpStatus.CREATED);
	}
	
	/**
	 * User token confirmation for account activation
	 */
	@GetMapping("/confirmation")
	public ResponseEntity<StringResponse> confirmation(
			@RequestParam("token") String token) {
		// confirm the user`s token
		this.pendingEmailService.confirmEmail(token);
		return ResponseEntity.ok(new StringResponse("Account confirmed"));
	}
	
	/**
	 * Resends the email with the verification token
	 * @return
	 */
	@GetMapping("/resend")
	public ResponseEntity<StringResponse> resend(
			@RequestParam("email") String email) {
		try {
			this.pendingEmailService.resetTokenEmail(email);
			// this.emailHelper.sendVerificationEmail(email);
			// TODO: AOP send verification email
		} catch (Exception e) {
			this.logger.error(e.getMessage());
			throw new IllegalStateException("Cannot send the token");
		}
		
		return ResponseEntity.ok(new StringResponse("Verification email sent"));
	}
}


















