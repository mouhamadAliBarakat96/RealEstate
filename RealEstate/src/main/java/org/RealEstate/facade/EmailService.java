package org.RealEstate.facade;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Stateless
public class EmailService {

//	@Resource(name = "java:jboss/mail/Default")
	private Session mailSession;

	public void sendVerificationCode(String toEmail) throws MessagingException {
		String code = generateVerificationCode();
		String subject = "Verification Code";
		String message = "Your verification code is: " + code;

		sendEmail(toEmail, subject, message);
	}

	private void sendEmail(String toEmail, String subject, String message) throws MessagingException {
		MimeMessage mimeMessage = new MimeMessage(mailSession);
		mimeMessage.setSubject(subject);
		mimeMessage.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(toEmail));
		mimeMessage.setContent(message, "text/plain");

		Transport.send(mimeMessage);
	}

	private String generateVerificationCode() {
		// Generate a random verification code (e.g., 6-digit code)
		Random random = new Random();
		int code = 100000 + random.nextInt(900000);
		return String.valueOf(code);
	}
}
