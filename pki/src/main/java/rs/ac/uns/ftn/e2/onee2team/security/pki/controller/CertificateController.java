package rs.ac.uns.ftn.e2.onee2team.security.pki.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.e2.onee2team.security.pki.service.ICertificateService;

@RestController
@RequestMapping(value = "/api/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateController {

	private ICertificateService certificateService;
	
	@Autowired
	public CertificateController(ICertificateService certificateService) {
		this.certificateService = certificateService;
	}
	
	@PostMapping(value = "/revoke/{num}")
	@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
	public void revokeCertificate(@PathVariable("num") Long serialNumber) {
		certificateService.revoke(serialNumber);
	}
}
