package rs.ac.uns.ftn.e2.onee2team.security.pki.service;

import java.util.List;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.Certificate;

public interface ICertificateService {

	void revoke(Long serialNumber);

	Boolean isRevoked(Long serialNumber);

	List<Certificate> allMyCertificates(String email);
}