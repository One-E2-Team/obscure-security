package rs.ac.uns.ftn.e2.onee2team.security.pki.dto;

public class RecoveryDTO {

	private Long id;
	private String uuid;
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
