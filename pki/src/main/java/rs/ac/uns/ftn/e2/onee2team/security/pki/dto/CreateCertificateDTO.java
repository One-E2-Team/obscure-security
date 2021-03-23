package rs.ac.uns.ftn.e2.onee2team.security.pki.dto;

import java.security.PublicKey;
import java.util.Date;
import java.util.List;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.CertificateExtension;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.CertificateSubject;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.CertificateType;

public class CreateCertificateDTO {
	private Long serialNumber;
	private Date startDate;
	private Date endDate;
	private PublicKey publicKey;
	private CertificateSubject subject;
	private Long issuerSerialNumber;
	private CertificateType type;
	private String signature;
	private List<CertificateExtension> extensions;
	private Boolean revoked;	
	
	public CreateCertificateDTO() {
		revoked = false;
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
	public PublicKey getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}
	public CertificateSubject getSubject() {
		return subject;
	}
	public void setSubject(CertificateSubject subject) {
		this.subject = subject;
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
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public List<CertificateExtension> getExtensions() {
		return extensions;
	}
	public void setExtensions(List<CertificateExtension> extensions) {
		this.extensions = extensions;
	}

	public Boolean getRevoked() {
		return revoked;
	}

	public void setRevoked(Boolean revoked) {
		this.revoked = revoked;
	}
	
	
}
