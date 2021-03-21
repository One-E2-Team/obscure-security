package rs.ac.uns.ftn.e2.onee2team.security.pki.model.users;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.Certificate;

@Entity
public class EndEntity extends User{

	@OneToMany(cascade = CascadeType.ALL)
	private List<Certificate> certificates;

	public List<Certificate> getCertificates() {
		return certificates;
	}

	public void setCertificates(List<Certificate> certificates) {
		this.certificates = certificates;
	}
}
