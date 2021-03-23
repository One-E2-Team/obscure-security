package rs.ac.uns.ftn.e2.onee2team.security.pki.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.Certificate;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.CertificateSubject;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.CertificateType;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.User;
import rs.ac.uns.ftn.e2.onee2team.security.pki.repository.ICertificateRepository;

@Service
public class CertificateService implements ICertificateService {
	
	private ICertificateRepository certificateRepository;
	private IUserService userServce;
	
	@Autowired
	public CertificateService(ICertificateRepository certificateRepository, IUserService userServce) {
		this.certificateRepository = certificateRepository;
		this.userServce = userServce;
	}

	@Override
	public void revoke(Long serialNumber) {
		Certificate cert = certificateRepository.findBySerialNumber(serialNumber);
		if (cert.getRevoked())
			return;
		
		cert.setRevoked(true);
		certificateRepository.save(cert);
		
		if (cert.getType().equals(CertificateType.END))
			return;
		
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
	
	private Certificate createCert(String email, String cn, Long ssn) {
		Certificate c = new Certificate();
		User u = userServce.findByEmail(email);
		Certificate issuer = certificateRepository.findBySerialNumber(ssn);
		if(u == null) return null;
		c.setSubject(new CertificateSubject());
		c.getSubject().setUserSubject(null);
		c.getSubject().setCommonName(cn);
		c.setIssuer(issuer.getSubject());
		return certificateRepository.save(c);
	}
}
