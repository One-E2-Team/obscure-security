package rs.ac.uns.ftn.e2.onee2team.security.pki.dto;

import javax.validation.constraints.NotBlank;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.UserType;

public class UserRequestDTO {
	@NotBlank(message = "Email cannot be empty.")
	private String email;

	@NotBlank(message = "Password cannot be empty.")
	private String password;
	
	private String country;
	
	private String state;
	
	private String locality;
	
	private String organization;
	
	private String organizationalUnit;

	private UserType userType;

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getOrganizationalUnit() {
		return organizationalUnit;
	}

	public void setOrganizationalUnit(String organizationalUnit) {
		this.organizationalUnit = organizationalUnit;
	}	
	
	
}
