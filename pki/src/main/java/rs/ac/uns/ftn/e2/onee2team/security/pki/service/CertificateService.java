package rs.ac.uns.ftn.e2.onee2team.security.pki.service;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.CreateCertificateDTO;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.Certificate;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.CertificateSubject;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.CertificateType;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.UserDefinedSubject;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.secret.KeyVault;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.secret.PublicKeyConverter;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.User;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.UserType;
import rs.ac.uns.ftn.e2.onee2team.security.pki.repository.ICertificateRepository;
import rs.ac.uns.ftn.e2.onee2team.security.pki.repository.IKeyVaultRepository;
import rs.ac.uns.ftn.e2.onee2team.security.pki.repository.IUserRepository;
import rs.ac.uns.ftn.e2.onee2team.security.pki.util.Base64Utility;
import rs.ac.uns.ftn.e2.onee2team.security.pki.util.RSA;

@Service
public class CertificateService implements ICertificateService {

	private ICertificateRepository certificateRepository;
	private IUserRepository userRepository;
	private IKeyVaultRepository keyVaultRepository;

	@Autowired
	public CertificateService(ICertificateRepository certificateRepository, IUserRepository userRepository, IKeyVaultRepository keyVaultRepository) {
		this.certificateRepository = certificateRepository;
		this.userRepository = userRepository;
		this.keyVaultRepository = keyVaultRepository;
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
	
	public Certificate createCert(CreateCertificateDTO ccdto) {
		Certificate c = new Certificate();
		User u = userRepository.findByEmail(ccdto.getEmail());
		Certificate issuer = certificateRepository.findBySerialNumber(ccdto.getIssuerSerialNumber());
		if(u == null || issuer == null) return null;
		c.setSubject(new CertificateSubject()); // TODO - check existence and appendence
		c.getSubject().setUserSubject(u.getUserSubject());
		c.getSubject().setCommonName(ccdto.getCommonName());
		c.setIssuer(issuer.getSubject());
		c.setSerialNumber(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
		c.setRevoked(false);
		c.setStartDate(ccdto.getStartDate());
		c.setEndDate(ccdto.getEndDate());
		c.setExtensions(ccdto.getExtensions());
		c.setType(ccdto.getType());
		c.setSignature("");
		if(ccdto.getType() == CertificateType.END) {
			PublicKey pk = (new PublicKeyConverter()).convertToEntityAttribute(ccdto.getPublicKey());
			KeyVault key = keyVaultRepository.findByPublicKey(pk);
			if(key == null) {
				KeyVault kv = new KeyVault();
				kv.setPrivateKey(null);
				kv.setPublicKey(pk);
				kv.setValidUntil(new Date((new Date()).getTime() + 315360000000L));
				kv = keyVaultRepository.save(kv);
				c.setPublicKey(kv.getPublicKey());
			} else if(key.getValidUntil().after(ccdto.getEndDate())){
				c.setPublicKey(pk);
			} else return null;
		} else {
			KeyPair kp = RSA.generateKeys();
			KeyVault kv = new KeyVault();
			kv.setPrivateKey(kp.getPrivate());
			kv.setPublicKey(kp.getPublic());
			kv.setValidUntil(new Date((new Date()).getTime() + 315360000000L));
			kv = keyVaultRepository.save(kv);
			c.setPublicKey(kv.getPublicKey());
		}
		return certificateRepository.save(c);
	}

	@Override
	public Boolean isIssuerValid(CreateCertificateDTO certificate) {
		Certificate issuer = certificateRepository.findBySerialNumber(certificate.getIssuerSerialNumber());		
		return issuer.canBeIssuerForDateRange(certificate.getStartDate(), certificate.getEndDate());
	}

	public List<Certificate> allMyCertificates(String email) {
		User user = userRepository.findByEmail(email);
		if(user.getUserType() == UserType.ADMINISTRATOR) {
			return certificateRepository.findAll();
		}
		return certificateRepository.findCertificatesByUserSubject(user.getUserSubject());
	}
}
