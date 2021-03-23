package rs.ac.uns.ftn.e2.onee2team.security.pki.controller;

import java.security.cert.Certificate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.CreateCertificateDTO;
import rs.ac.uns.ftn.e2.onee2team.security.pki.service.ICertificateService;

@RestController
@RequestMapping(value = "/api/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateController {

	private ICertificateService certificateService;
	
	@Autowired
	public CertificateController(ICertificateService certificateService) {
		this.certificateService = certificateService;
	}
	
	@PostMapping("/revoke")
	public void revokeCertificate(@RequestParam Long serialNumber) {
		certificateService.revoke(serialNumber);
	}
	@PostMapping("/is-valid")
	public boolean isCertificateValidate(@RequestBody CreateCertificateDTO certificate) {
		return certificateService.isCertificateValid(certificate);
	}
}
