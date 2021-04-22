package rs.ac.uns.ftn.e2.onee2team.security.pki.service;

import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.CreateCertificateDTO;
import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.PublicKeysDTO;
import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.UserCommonNameDTO;

import java.util.List;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.Certificate;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.User;

public interface ICertificateService {

	void revoke(String serialNumber);

	Boolean isRevoked(String serialNumber);

	List<Certificate> allMyCertificates(String email);
	
	Certificate createCert(CreateCertificateDTO ccdto);
	
	List<PublicKeysDTO> getAvailablePublicKeys(String email);

	byte[] certDownloader(String ssn);

	Boolean isIssuerValid(CreateCertificateDTO certificate, User user);
	
	UserCommonNameDTO findUserBySerialNumber(String serialNumber);
}
