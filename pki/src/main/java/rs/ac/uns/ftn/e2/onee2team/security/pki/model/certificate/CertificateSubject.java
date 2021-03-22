package rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "certificate_subject")
public class CertificateSubject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "commonName", nullable = false, unique = true)
	private String commonName;
	
	@ManyToOne(optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "userSubject")
	private UserDefinedSubject userSubject;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
