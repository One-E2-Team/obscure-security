package rs.ac.uns.ftn.e2.onee2team.security.pki.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService implements IEmailNotificationService {

	private JavaMailSender javaMailSender;
	private Environment env;

	@Autowired
	public EmailNotificationService(JavaMailSender javaMailSender, Environment env) {
		this.javaMailSender = javaMailSender;
		this.env = env;
	}

	@Async
	public void sendNotificationAsync(String sendTo, String subject, String mailMessage) {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(sendTo);
		mail.setFrom(env.getProperty("spring.mail.username"));
		mail.setSubject(subject);
		mail.setText(mailMessage);
		try {
			javaMailSender.send(mail);
		} catch (Exception e) {
			System.out.println(e);
		}

	}
}
