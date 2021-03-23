package rs.ac.uns.ftn.e2.onee2team.security.pki.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.Certificate;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.CertificateSubject;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.CertificateType;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.UserDefinedSubject;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.User;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.UserType;
import rs.ac.uns.ftn.e2.onee2team.security.pki.repository.ICertificateRepository;
import rs.ac.uns.ftn.e2.onee2team.security.pki.repository.IUserRepository;

@Service
public class CertificateService implements ICertificateService {

	private ICertificateRepository certificateRepository;
	private IUserRepository userRepository;

	@Autowired
	public CertificateService(ICertificateRepository certificateRepository, IUserRepository userRepository) {
		this.certificateRepository = certificateRepository;
		this.userRepository = userRepository;
	}

	@Override
	public void revoke(Long serialNumber) {
		System.out.println(certificateRepository.findAll());
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
		User u = userRepository.findByEmail(email);
		Certificate issuer = certificateRepository.findBySerialNumber(ssn);
		if(u == null) return null;
		c.setSubject(new CertificateSubject());
		c.getSubject().setUserSubject(null);
		c.getSubject().setCommonName(cn);
		c.setIssuer(issuer.getSubject());
		return certificateRepository.save(c);
	}

	@Override
	public List<Certificate> allMyCertificates(String email) {
		User user = userRepository.findByEmail(email);
		if(user.getUserType() == UserType.ADMINISTRATOR) {
			return certificateRepository.findAll();
		}
		return certificateRepository.findCertificatesByUserSubject(user.getUserSubject());
	}
}
