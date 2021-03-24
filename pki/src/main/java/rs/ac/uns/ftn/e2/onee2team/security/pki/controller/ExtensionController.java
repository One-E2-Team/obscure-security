package rs.ac.uns.ftn.e2.onee2team.security.pki.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.AvailableExtension;
import rs.ac.uns.ftn.e2.onee2team.security.pki.service.IExtensionService;

@RestController
@RequestMapping(value= "/api/extensions", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExtensionController {
	private IExtensionService extensionService;

	@Autowired
	public ExtensionController(IExtensionService extensionService) {
		this.extensionService = extensionService;
	}
	
	@GetMapping
	@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')" + "||" + "hasRole('ROLE_INTERMEDIARY_CA')")
	public List<AvailableExtension> getAll(){
		return extensionService.getAll();
	}
}
