package rs.ac.uns.ftn.e2.onee2team.security.pki.service;

public interface ICertificateService {

	void revoke(Long serialNumber);
	
	Boolean isRevoked(Long serialNumber);
}
