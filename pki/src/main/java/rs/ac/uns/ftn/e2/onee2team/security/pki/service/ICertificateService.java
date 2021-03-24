package rs.ac.uns.ftn.e2.onee2team.security.pki.service;

import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.CreateCertificateDTO;
import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.PublicKeysDTO;

import java.util.List;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.Certificate;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.User;

public interface ICertificateService {

	void revoke(Long serialNumber);

	Boolean isRevoked(Long serialNumber);

	List<Certificate> allMyCertificates(String email);
	
	Certificate createCert(CreateCertificateDTO ccdto);
	
	List<PublicKeysDTO> getAvailablePublicKeys(String email);

	byte[] certDownloader(Long ssn);

	Boolean isIssuerValid(CreateCertificateDTO certificate, User user);
}
