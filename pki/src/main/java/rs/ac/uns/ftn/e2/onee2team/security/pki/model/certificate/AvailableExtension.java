package rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "available_extensions")
public class AvailableExtension {

	@Id
	@Column(name = "name", unique = true, nullable = false)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
