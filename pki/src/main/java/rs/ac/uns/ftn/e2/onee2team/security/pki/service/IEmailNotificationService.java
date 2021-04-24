package rs.ac.uns.ftn.e2.onee2team.security.pki.service;


public interface IEmailNotificationService {
	void sendNotificationAsync(String sendTo, String subject, String mailMessage);
}
