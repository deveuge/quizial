package com.deveuge.quizial.model.service;

import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deveuge.quizial.enums.VerificationType;
import com.deveuge.quizial.model.dao.VerificationTokenDAO;
import com.deveuge.quizial.model.entity.User;
import com.deveuge.quizial.model.entity.VerificationToken;
import com.deveuge.quizial.util.FileUtils;

@Service
public class VerificationTokenService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
    private MessageSource messageSource;
	@Value("${app.base.url}")
	private String baseUrl;

	@Autowired
	private VerificationTokenDAO verificationTokenDAO;

	@Transactional(readOnly=true)
	public VerificationToken findOne(String id) {
		return verificationTokenDAO.findById(id).orElse(null);
	}

	@Transactional
	public void save(VerificationToken verificationToken) {
		verificationTokenDAO.save(verificationToken);
	}

	@Transactional
	public void delete(String token) {
		verificationTokenDAO.deleteById(token);
	}
	
	/**
	 * Sends an email to the specified user
	 * @param user {@link User} User who will receive the email
	 * @param type {@link VerificationType} Type of verification (for registration or recovery emails)
	 * @param locale {@link Locale} Specific geographical, political, or cultural region
	 * @return {@link boolean} True if the email is sent successfully, false otherwise
	 */
	@Async
	@Transactional
	public boolean sendEmail(User user, VerificationType type, Locale locale) {
		try {
			VerificationToken verificationToken = createVerificationToken(user, type);
			MimeMessage message = buildEmail(user, verificationToken, locale);
			javaMailSender.send(message);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Creates a new verification token for the specified user, removing all the previous ones
	 * @param user {@link User} User for whom the token will be created
	 * @param type {@link VerificationType} Type of verification token
	 * @return {@link VerificationToken} The newly generated token
	 */
	private VerificationToken createVerificationToken(User user, VerificationType type) {
		VerificationToken verificationToken = new VerificationToken(user, type);
		verificationTokenDAO.deleteByUser(user);
		verificationTokenDAO.save(verificationToken);
		return verificationToken;
	}
	
	private MimeMessage buildEmail(User user, VerificationToken verificationToken, Locale locale) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		String type = verificationToken.getType().name().toLowerCase();
		helper.setTo(InternetAddress.parse(user.getEmail()));
		helper.setSubject("[Quizial] " + getMessage(String.format("email.%s.title", type), user.getUsername(), locale));
		helper.setText(buildEmailBody(user, verificationToken, locale), true);
		return message;
	}
	
	private String buildEmailBody(User user, VerificationToken verificationToken, Locale locale) {
		String body = FileUtils.readBodyFile("emailTemplate.html");
		String type = verificationToken.getType().name().toLowerCase();
		String link = String.format("%s/%s?token=%s", baseUrl, type, verificationToken.getToken());
		
		body = body
			.replace("${title}", getMessage(String.format("email.%s.title", type), user.getUsername(), locale))
			.replace("${subtitle}", getMessage(String.format("email.%s.subtitle", type), locale))
			.replace("${body}", getMessage(String.format("email.%s.body", type), verificationToken.getUUIDTokenPart(), locale))
			.replace("${button}", getMessage(String.format("email.%s.button", type), locale))
			.replace("${buttonUrl}", link)
			.replace("${appUrl}", baseUrl)
			.replace("${disclaimer}", getMessage("email.disclaimer", locale));
		return body;
	}
	
	private String getMessage(String key, String param, Locale locale) {
		return messageSource.getMessage(key, new Object[] {param}, locale);
	}
	
	private String getMessage(String key, Locale locale) {
		return getMessage(key, null, locale);
	}
}
