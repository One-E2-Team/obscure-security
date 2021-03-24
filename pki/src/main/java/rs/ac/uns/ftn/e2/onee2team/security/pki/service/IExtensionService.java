package rs.ac.uns.ftn.e2.onee2team.security.pki.service;

import java.util.List;

import rs.ac.uns.ftn.e2.onee2team.security.pki.model.certificate.AvailableExtension;

public interface IExtensionService {
	List<AvailableExtension> getAll();
}
