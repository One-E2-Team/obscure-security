package rs.ac.uns.ftn.e2.onee2team.security.pki.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.CreateCertificateDTO;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.Certificate;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.CertificateType;
import rs.ac.uns.ftn.e2.onee2team.security.pki.repository.ICertificateRepository;

@Service
public class CertificateService implements ICertificateService {
	
	private ICertificateRepository certificateRepository;
	
	@Autowired
	public CertificateService(ICertificateRepository certificateRepository) {
		this.certificateRepository = certificateRepository;
	}

	@Override
	public void revoke(Long serialNumber) {
		Certificate cert = certificateRepository.findBySerialNumber(serialNumber);
		if (cert.getRevoked())
			return;
		if (cert.getType().equals(CertificateType.END)){
			cert.setRevoked(true);
			certificateRepository.save(cert);
			return;
		}
		revokeChildren(cert.getSubject().getId());
	}
	
	private void revokeChildren(Long subjectId) {
		for(Certificate c : certificateRepository.findCertificatesByIssuerId(subjectId)) {
			if(c.getType().equals(CertificateType.INTERMEDIATE))
				revokeChildren(c.getSubject().getId());
			c.setRevoked(true);
			certificateRepository.save(c);
		}
	}

	@Override
	public Boolean isRevoked(Long serialNumber) {
		return certificateRepository.findBySerialNumber(serialNumber).getRevoked();
	}

	@Override
	public Boolean isCertificateValid(CreateCertificateDTO certificate) {
		Certificate issuer = certificateRepository.findBySerialNumber(certificate.getIssuerSerialNumber());		
		return issuer.canBeIssuerForDateRange(certificate.getStartDate(), certificate.getEndDate()) && !certificate.getRevoked();
	}
}
