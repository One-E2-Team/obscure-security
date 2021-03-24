package rs.ac.uns.ftn.e2.onee2team.security.pki.controller;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.Certificate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.CreateCertificateDTO;
import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.PublicKeysDTO;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.User;
import rs.ac.uns.ftn.e2.onee2team.security.pki.service.ICertificateService;

@RestController
@RequestMapping(value = "/api/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateController {

	private ICertificateService certificateService;

	@Autowired
	public CertificateController(ICertificateService certificateService) {
		this.certificateService = certificateService;
	}

	@GetMapping(value = "/my")
	@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')" + "||" + "hasRole('ROLE_INTERMEDIARY_CA')" + "||"
			+ "hasRole('ROLE_END_ENTITY')")
	public List<Certificate> allMyCertificates(Authentication auth) {
		User user = (User) auth.getPrincipal();
		return certificateService.allMyCertificates(user.getEmail());
	}

	@PostMapping(value = "/revoke/{num}")
	@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
	public void revokeCertificate(@PathVariable("num") Long serialNumber) {
		certificateService.revoke(serialNumber);
	}
	
	@PostMapping("/is-valid")
	public boolean isCertificateValidate(@RequestBody CreateCertificateDTO certificate) {
		return certificateService.isIssuerValid(certificate);
	}
	
	@GetMapping("/isRevoked/{num}")
	@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')" + "||" + "hasRole('ROLE_INTERMEDIARY_CA')" + "||"
			+ "hasRole('ROLE_END_ENTITY')")
	public boolean isCertificateRevoke(@PathVariable("num") Long serialNumber) {
		return certificateService.isRevoked(serialNumber);
	}
	
	@PostMapping(value = "/create")
	@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')" + "||" + "hasRole('ROLE_INTERMEDIARY_CA')")
	public Certificate create(@RequestBody CreateCertificateDTO ccdto) {
		System.out.println(ccdto.getStartDate());
		System.out.println(ccdto.getEndDate());
		if(certificateService.isIssuerValid(ccdto))
			return certificateService.createCert(ccdto);
		else return null;
	}
	
	@GetMapping(value = "/issuerpubkeys")
	@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')" + "||" + "hasRole('ROLE_INTERMEDIARY_CA')")
	public List<PublicKeysDTO> getPubKeys(@RequestBody String email) {
		return certificateService.getAvailablePublicKeys(email);
	}
}
