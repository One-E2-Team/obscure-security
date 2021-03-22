package rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "certificates")
public class Certificate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "serialNumber", nullable = false, unique = true)
	private Long serialNumber;
	
	@Column(name = "startDate", nullable = false)
	private Date startDate;
	
	@Column(name = "endDate", nullable = false)
	private Date endDate;
	
	@Column(name = "publicKey", nullable = false, unique = true)
	private String publicKey;
	
	@ManyToOne(optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "subject_id")
	private CertificateSubject subject;

	@ManyToOne(optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "issuer_id")
	private CertificateSubject issuer;
	
	@Column(name = "type", nullable = false)
	private CertificateType type;
	
	@Column(name = "revoked", nullable = false)
	private Boolean revoked;
	
	@Column(name = "signature", length = 1024, nullable = false)
	private String signature;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "extensions")
	private List<CertificateExtension> extensions;
	
	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public CertificateSubject getSubject() {
		return subject;
	}

	public void setSubject(CertificateSubject subject) {
		this.subject = subject;
	}

	public CertificateSubject getIssuer() {
		return issuer;
	}

	public void setIssuer(CertificateSubject issuer) {
		this.issuer = issuer;
	}

	public List<CertificateExtension> getExtensions() {
		return extensions;
	}

	public void setExtensions(List<CertificateExtension> extensions) {
		this.extensions = extensions;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Long serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public CertificateType getType() {
		return type;
	}

	public void setType(CertificateType type) {
		this.type = type;
	}

	public Boolean getRevoked() {
		return revoked;
	}

	public void setRevoked(Boolean revoked) {
		this.revoked = revoked;
	}
}
