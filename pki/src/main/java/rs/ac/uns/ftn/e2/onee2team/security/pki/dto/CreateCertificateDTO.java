package rs.ac.uns.ftn.e2.onee2team.security.pki.dto;

import java.util.Date;
import java.util.List;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.CertificateExtension;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.CertificateType;

public class CreateCertificateDTO {
	private Date startDate;
	private Date endDate;
	private String commonName;
	private String email;
	private String publicKey;
	private Long issuerSerialNumber;
	private CertificateType type;
	private List<CertificateExtension> extensions;
	
	public CreateCertificateDTO() {
		
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
	public Long getIssuerSerialNumber() {
		return issuerSerialNumber;
	}
	public void setIssuerSerialNumber(Long issuerSerialNumber) {
		this.issuerSerialNumber = issuerSerialNumber;
	}
	public CertificateType getType() {
		return type;
	}
	public void setType(CertificateType type) {
		this.type = type;
	}
	public List<CertificateExtension> getExtensions() {
		return extensions;
	}
	public void setExtensions(List<CertificateExtension> extensions) {
		this.extensions = extensions;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	
	
}
