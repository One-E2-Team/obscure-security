package rs.ac.uns.ftn.e2.onee2team.security.pki.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.CertificateExtension;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.CertificateType;

public class CreateCertificateDTO {
	
	@NotNull(message = "Start date cannot be null.")
	private Date startDate;
	
	@NotNull(message = "End date cannot be null.")
	private Date endDate;
	
	@NotBlank(message = "Common name cannot be empty.")
	private String commonName;
	
	@NotBlank(message = "Email cannot be empty.")
	private String email;
	
	@NotNull(message = "Public key cannot be empty.")
	private String publicKey;
	
	private String issuerSerialNumber;
	
	@NotNull(message = "Certificate type cannot be null.")
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
	public String getIssuerSerialNumber() {
		return issuerSerialNumber;
	}
	public void setIssuerSerialNumber(String issuerSerialNumber) {
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
