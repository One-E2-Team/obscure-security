package rs.ac.uns.ftn.e2.onee2team.security.pki.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class RecoveryDTO {

	private Long id;
	
	@NotBlank(message = "UUID cannot be empty.")
	private String uuid;
	
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[*.!@#$%^&(){}\\\\[\\\\]:;<>,.?~_+-=|\\\\/])[A-Za-z0-9*.!@#$%^&(){}\\\\[\\\\]:;<>,.?~_+-=|\\\\/]{8,}$")
	@PasswordConstraint
	private String password;

	public RecoveryDTO(Long id, String uuid, String password) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
