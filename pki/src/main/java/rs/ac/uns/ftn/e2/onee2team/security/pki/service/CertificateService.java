package rs.ac.uns.ftn.e2.onee2team.security.pki.service;

import java.security.KeyPair;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.CreateCertificateDTO;
import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.PublicKeysDTO;
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
import rs.ac.uns.ftn.e2.onee2team.security.pki.util.CertificateGenerator;
import rs.ac.uns.ftn.e2.onee2team.security.pki.util.IssuerData;
import rs.ac.uns.ftn.e2.onee2team.security.pki.util.RSA;
import rs.ac.uns.ftn.e2.onee2team.security.pki.util.SubjectData;

@Service
public class CertificateService implements ICertificateService {

	private ICertificateRepository certificateRepository;
	private IUserRepository userRepository;
	private IKeyVaultRepository keyVaultRepository;
	
	private final Long ROOT_MAX_VALUE = 315569520000L; // 10 years
	private final Long INTERMEDIATE_MAX_VALUE = 157784760000L; // 5 years
	private final Long END_ENTITY_MAX_VALUE = 31556952000L; // 1 year

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
		if(!validDates(certificate.getStartDate(), certificate.getEndDate(), certificate.getType()))
			return false;
		Certificate issuer = certificateRepository.findBySerialNumber(certificate.getIssuerSerialNumber());		
		return issuer.canBeIssuerForDateRange(certificate.getStartDate(), certificate.getEndDate());
	}
	
	private Boolean validDates(Date start, Date end, CertificateType type) {
		if(end.before(start) || start.before(new Date()))
			return false;
		if(type.equals(CertificateType.ROOT)) {
			if ((start.getTime() + ROOT_MAX_VALUE) < end.getTime()) 
				return false;
		}
		else if(type.equals(CertificateType.INTERMEDIATE)) {
			if ((start.getTime() + INTERMEDIATE_MAX_VALUE) < end.getTime()) 
				return false;
		}
		else if(type.equals(CertificateType.END)) {
			if ((start.getTime() + END_ENTITY_MAX_VALUE) < end.getTime())
				return false;
		}
		return true;
	}

	public List<Certificate> allMyCertificates(String email) {
		User user = userRepository.findByEmail(email);
		if(user.getUserType() == UserType.ADMINISTRATOR) {
			return certificateRepository.findAll();
		}
		return certificateRepository.findCertificatesByUserSubject(user.getUserSubject());
	}

	@Override
	public List<PublicKeysDTO> getAvailablePublicKeys(String email) {
		User u = userRepository.findByEmail(email);
		List<Certificate> cs = certificateRepository.findTrustedValidCertificatesByUserSubjectInSubject(u.getUserSubject());
		ArrayList<PublicKeysDTO> pbkdto = new ArrayList<PublicKeysDTO>();
		for (Certificate c : cs) {
			KeyVault kv = keyVaultRepository.findByPublicKey(c.getPublicKey());
			PublicKeysDTO temp = new PublicKeysDTO();
			temp.setPublicKey(Base64Utility.encode(kv.getPublicKey().getEncoded()));
			temp.setValidUntil(kv.getValidUntil());
			if(kv.getPrivateKey() != null) temp.setHasPrivate(true);
			else temp.setHasPrivate(false);
			pbkdto.add(temp);
		}
		return pbkdto;
	}
	
	@Override
	public byte[] certDownloader(Long ssn) {
		Certificate cert = certificateRepository.findBySerialNumber(ssn);
		Certificate issuerCert = certificateRepository.findCurrentValidCertificateByIssuerAndSubjectCertDates(cert.getIssuer(), cert.getStartDate(), cert.getEndDate());
		KeyVault isssuerkv = keyVaultRepository.findByPublicKey(issuerCert.getPublicKey());
		X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
	    builder.addRDN(BCStyle.CN, issuerCert.getSubject().getCommonName());
	    builder.addRDN(BCStyle.O, issuerCert.getSubject().getUserSubject().getOrganization());
	    builder.addRDN(BCStyle.OU, issuerCert.getSubject().getUserSubject().getOrganizationalUnit());
	    builder.addRDN(BCStyle.C, issuerCert.getSubject().getUserSubject().getCountry());
	    builder.addRDN(BCStyle.ST, issuerCert.getSubject().getUserSubject().getState());
	    builder.addRDN(BCStyle.L, issuerCert.getSubject().getUserSubject().getLocality());
	    User issuer = userRepository.findUserByUserDefinedSubject(issuerCert.getSubject().getUserSubject());
	    builder.addRDN(BCStyle.E, issuer.getEmail());
	    builder.addRDN(BCStyle.UID, issuer.getId().toString());
		IssuerData issuerData = new IssuerData(isssuerkv.getPrivateKey(), builder.build());
		builder = new X500NameBuilder(BCStyle.INSTANCE);
	    builder.addRDN(BCStyle.CN, cert.getSubject().getCommonName());
	    builder.addRDN(BCStyle.O, cert.getSubject().getUserSubject().getOrganization());
	    builder.addRDN(BCStyle.OU, cert.getSubject().getUserSubject().getOrganizationalUnit());
	    builder.addRDN(BCStyle.C, cert.getSubject().getUserSubject().getCountry());
	    builder.addRDN(BCStyle.ST, cert.getSubject().getUserSubject().getState());
	    builder.addRDN(BCStyle.L, cert.getSubject().getUserSubject().getLocality());
	    User subject = userRepository.findUserByUserDefinedSubject(cert.getSubject().getUserSubject());
	    builder.addRDN(BCStyle.E, subject.getEmail());
	    builder.addRDN(BCStyle.UID, subject.getId().toString());
	    SubjectData subjectData = new SubjectData(cert.getPublicKey(), builder.build(), cert.getSerialNumber().toString(), cert.getStartDate(), cert.getEndDate());
	    CertificateGenerator cg = new CertificateGenerator();
		X509Certificate ret = cg.generateCertificate(subjectData, issuerData);
		System.out.println(ret);
		try {
			return ret.getEncoded();
		} catch (CertificateEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
