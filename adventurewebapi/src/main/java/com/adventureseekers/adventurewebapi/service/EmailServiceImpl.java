package com.adventureseekers.adventurewebapi.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
	
	private Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	@Async
	public void send(String to, String email, String subject) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper =
					new MimeMessageHelper(mimeMessage, "utf-8");
			helper.setText(email, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setFrom("no-reply@adventureseekers.go.ro");
			this.mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			this.logger.error("failed to send email", e);
			throw new IllegalStateException("failed to send email");
		}
	}
	
}
