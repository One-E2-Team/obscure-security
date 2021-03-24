package rs.ac.uns.ftn.e2.onee2team.security.pki.dto;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.UserDefinedSubject;

public class UserCommonNameDTO {

	private String commonName;
	private UserDefinedSubject userSubject;
	
	public UserCommonNameDTO() {}
	
	public UserCommonNameDTO(String commonName, UserDefinedSubject userSubject) {
		super();
		this.commonName = commonName;
		this.userSubject = userSubject;
	}

	public String getCommonName() {
		return commonName;
	}
	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}
	public UserDefinedSubject getUserSubject() {
		return userSubject;
	}
	public void setUserSubject(UserDefinedSubject userSubject) {
		this.userSubject = userSubject;
	}
}
