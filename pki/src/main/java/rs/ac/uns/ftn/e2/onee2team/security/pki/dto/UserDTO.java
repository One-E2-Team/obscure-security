package rs.ac.uns.ftn.e2.onee2team.security.pki.dto;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.UserDefinedSubject;

public class UserDTO {

	private String email;
	private UserDefinedSubject userSubject;
	
	public UserDTO() {
		
	}
	public UserDTO(String email, UserDefinedSubject userSubject) {
		super();
		this.email = email;
		this.userSubject = userSubject;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public UserDefinedSubject getUserSubject() {
		return userSubject;
	}
	public void setUserSubject(UserDefinedSubject userSubject) {
		this.userSubject = userSubject;
	}
}
