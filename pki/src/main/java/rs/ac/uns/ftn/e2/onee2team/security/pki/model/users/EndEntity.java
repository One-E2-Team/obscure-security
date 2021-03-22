package rs.ac.uns.ftn.e2.onee2team.security.pki.model.users;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.Certificate;

@Entity
@DiscriminatorValue("END_ENTITY")
public class EndEntity extends User{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Certificate> certificates;

	public List<Certificate> getCertificates() {
		return certificates;
	}

	public void setCertificates(List<Certificate> certificates) {
		this.certificates = certificates;
	}
}
