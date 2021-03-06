package rs.ac.uns.ftn.e2.onee2team.security.pki.controller;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.Certificate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.CreateCertificateDTO;
import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.PublicKeysDTO;
import rs.ac.uns.ftn.e2.onee2team.security.pki.dto.UserCommonNameDTO;
import rs.ac.uns.ftn.e2.onee2team.security.pki.model.users.User;
import rs.ac.uns.ftn.e2.onee2team.security.pki.service.ICertificateService;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateController extends ValidationController {

	private ICertificateService certificateService;

	@Autowired
	public CertificateController(ICertificateService certificateService) {
		this.certificateService = certificateService;
	}
	
	@GetMapping(value = "/my")
	@PreAuthorize("hasAuthority('READ_CERT')")
	public List<Certificate> allMyCertificates(Authentication auth) {
		User user = (User) auth.getPrincipal();
		return certificateService.allMyCertificates(user.getEmail());
	}
	
	@GetMapping(value = "/user/{num}")
	@PreAuthorize("hasAuthority('READ_USERS')")
	public UserCommonNameDTO getUserBySerialNumber(@PathVariable("num") String serialNumber) {
		return certificateService.findUserBySerialNumber(serialNumber);
	}

	@PostMapping(value = "/revoke/{num}")
	@PreAuthorize("hasAuthority('DELETE_CERT')")
	public void revokeCertificate(@PathVariable("num") String serialNumber) {
		certificateService.revoke(serialNumber);
	}
	
	@GetMapping("/isRevoked/{num}")
	@PreAuthorize("hasAuthority('READ_CERT')")
	public boolean isCertificateRevoke(@PathVariable("num") String serialNumber) {
		return certificateService.isRevoked(serialNumber);
	}
	
	@PostMapping(value = "/create")
	@PreAuthorize("hasAuthority('CREATE_CERT')")
	public Certificate create(@Valid @RequestBody CreateCertificateDTO ccdto, Authentication auth) {
		User user = (User) auth.getPrincipal();
		if(certificateService.isIssuerValid(ccdto, user))
			return certificateService.createCert(ccdto);
		else return null;
	}
	
	@PostMapping(value = "/issuerpubkeys")
	@PreAuthorize("hasAuthority('READ_PUBLIC_KEYS')")
	public List<PublicKeysDTO> getPubKeys(@RequestBody String email) {
		return certificateService.getAvailablePublicKeys(email);
	}
	
	@GetMapping(value = "/download/{ssn}")
	public ResponseEntity<Resource> download(@PathVariable("ssn") String ssn){
		HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + ssn.toString() + ".cer");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        ByteArrayResource resource = new ByteArrayResource(certificateService.certDownloader(ssn));

        return ResponseEntity.ok()
                .headers(header)
                //.contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
	}
}
