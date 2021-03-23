package rs.ac.uns.ftn.e2.onee2team.security.pki.service;

import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.CreateCertificateDTO;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.Certificate;

public interface ICertificateService {

	void revoke(Long serialNumber);
	
	Boolean isRevoked(Long serialNumber);
	
	Boolean isCertificateValid(CreateCertificateDTO certificate);
}
